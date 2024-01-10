package de.codelix.commandapi.core;

import de.codelix.commandapi.core.design.CoreDefaultMessages;
import de.codelix.commandapi.core.exception.*;
import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.design.DefaultCommandDesign;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.tree.CommandNode;
import de.codelix.commandapi.core.tree.LiteralCommandNode;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.core.parser.StringReader;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Placeblock
 */
@Getter
public abstract class Command<S> {

    public static Logger LOGGER;
    public static CommandDesign DESIGN = new DefaultCommandDesign();

    static {
        LOGGER = Logger.getLogger("commandapi");
        LOGGER.setLevel(Level.WARNING);
        new CoreDefaultMessages().register();
    }

    private final LiteralCommandNode<S> base;
    private final TextComponent prefix;
    private final boolean async;
    private final ExecutorService threadPool;
    private final CommandDesign design;

    public Command(String label) {
        this(label, false);
    }

    public Command(String label, CommandDesign commandDesign) {
        this(label, false, commandDesign);
    }

    public Command(String label, boolean async) {
        this(label, async, DESIGN);
    }

    public Command(String label, boolean async, CommandDesign commandDesign) {
        this.design = commandDesign;
        this.async = async;
        CommandNode<S> baseCommand = this.generateCommand(new LiteralCommandNodeBuilder<>(label)).build(this);
        if (!(baseCommand instanceof LiteralCommandNode<S> literalTreeCommand)) {
            throw new IllegalArgumentException("You can only use LiteralTreeCommandBuilder as root");
        }
        this.base = literalTreeCommand;
        this.prefix = this.design.getPrefix(this);
        if (this.async) {
            this.threadPool = Executors.newFixedThreadPool(4);
        } else {
            this.threadPool = null;
        }
    }

    public abstract LiteralCommandNodeBuilder<S> generateCommand(LiteralCommandNodeBuilder<S> builder);

    public abstract boolean hasPermission(S source, String permission);

    public void sendMessage(S source, TextComponent message) {
        this.sendMessage(source, message, true);
    }
    public void sendMessage(S source, TextComponent message, boolean prefix) {
        if (prefix && this.prefix != null) {
            message = this.prefix.append(Component.empty().style(Style.empty())).append(message);
        }
        this.sendMessageRaw(source, message);
    }

    public abstract void sendMessageRaw(S source, TextComponent message);

    public void parseAndExecute(String text, S source) {
        List<ParsedCommandBranch<S>> parseResult = this.parse(text, source);
        this.execute(getBestResult(parseResult), source);
    }

    public List<ParsedCommandBranch<S>> parse(String text, S source) {
        ParsedCommandBranch<S> parsedCommandBranch = new ParsedCommandBranch<>(new StringReader(text));
        return this.base.parseRecursive(parsedCommandBranch, source);
    }

    public void execute(ParsedCommandBranch<S> result, S source) {
        try {
            this.executeRaw(result, source);
        } catch (CommandHelpException e) {
            TextComponent message = this.design.generateHelpMessage(this, source);
            this.sendMessage(source, message, false);
        } catch (CommandParseException e) {
            TextComponent message = this.design.getMessage(e);
            if (message == null) {
                this.sendMessage(source, Component.text("Missing Exception Message for Exception: " + e.getClass().getSimpleName()));
                return;
            }
            this.sendMessage(source, message);
        }
    }

    public void executeRaw(ParsedCommandBranch<S> result, S source) throws CommandParseException {
        Command.LOGGER.info("Command for Execution: " + result.getBranch().stream().map(CommandNode::getName).toList());
        Command.LOGGER.info("Command for Execution2: " + result.getReader().debugString());
        CommandNode<S> lastParsed = result.getLastParsedTreeCommand();
        if (result.getException() != null) {
            if (result.getException() instanceof InvalidLiteralException) {
                throw new CommandHelpException();
            } else {
                throw result.getException();
            }
        }
        CommandExecutor<S> commandExecutor = lastParsed.getCommandExecutor();
        if (commandExecutor == null) {
            throw new CommandHelpException();
        } else {
            commandExecutor.run(result, source);
        }
    }

    public List<String> getSuggestions(List<ParsedCommandBranch<S>> results, S source) {
        List<String> suggestions = new ArrayList<>();
        for (ParsedCommandBranch<S> result : results) {
            Command.LOGGER.info("Checking Result: " + result.getBranch().stream().map(CommandNode::getName).toList());
            StringReader reader = result.getReader();
            String remaining = reader.getRemaining();
            // We only get suggestions if the remaining text starts with a whitespace and the next char is no whitespace
            if (remaining.startsWith(" ") && !remaining.substring(1).startsWith(" ")) {
                reader.skip();
                CommandNode<S> lastParsedCommandNode = result.getLastParsedTreeCommand();
                List<CommandNode<S>> suggestionCommandNodes;
                // If the last command was parsed successfully we get suggestions for the child commands
                if (result.getException() != null) {
                    suggestionCommandNodes = List.of(lastParsedCommandNode);
                } else {
                    suggestionCommandNodes = lastParsedCommandNode.getChildren();
                }
                for (CommandNode<S> suggestionCommandNode : suggestionCommandNodes) {
                    Command.LOGGER.info("Getting Suggestions for TreeCommand: " + suggestionCommandNode.getName());
                    List<String> resultSuggestions = suggestionCommandNode.getSuggestions(result, source);
                    Command.LOGGER.info(resultSuggestions.toString());
                    suggestions.addAll(resultSuggestions);
                }
            }
        }
        return suggestions;
    }

    /**
     * Returns the best Result for a list of ParsedCommands
     */
    public static <S> ParsedCommandBranch<S> getBestResult(List<ParsedCommandBranch<S>> results) {
        results.sort((a, b) -> {
            if (a.getBranch().size() > b.getBranch().size()) return -1;
            if (a.getBranch().size() < b.getBranch().size()) return 1;
            if (!a.getReader().canRead() && b.getReader().canRead()) return -1;
            if (a.getReader().canRead() && !b.getReader().canRead()) return 1;
            if (a.getException() == null && b.getException() != null) return -1;
            if (a.getException() != null && b.getException() == null) return 1;
            CommandExecutor<S> aCommandExecutor = a.getLastParsedTreeCommand().getCommandExecutor();
            CommandExecutor<S> bCommandExecutor = b.getLastParsedTreeCommand().getCommandExecutor();
            if (aCommandExecutor != null && bCommandExecutor == null) return -1;
            if (aCommandExecutor == null && bCommandExecutor != null) return 1;
            return 0;
        });
        Command.LOGGER.info("Sorted Parsed Commands:");
        for (ParsedCommandBranch<S> parsedCommandBranch : results) {
            Command.LOGGER.info(parsedCommandBranch.getBranch().stream().map(CommandNode::getName).toList() + ": " + parsedCommandBranch.getReader().debugString());
            Command.LOGGER.info("Tree Commands:" + parsedCommandBranch.getBranch().size());
            Command.LOGGER.info("Exception:" + parsedCommandBranch.getException());
            if (parsedCommandBranch.getLastParsedTreeCommand() != null) {
                Command.LOGGER.info("Executor:" + parsedCommandBranch.getLastParsedTreeCommand().getCommandExecutor());
            }
        }
        return results.get(0);
    }
}
