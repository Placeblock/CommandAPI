package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.exception.CommandHelpException;
import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.messages.Messages;
import de.placeblock.commandapi.core.parser.ParsedCommandBranch;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.parser.StringReader;
import io.schark.design.colors.Colors;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

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
    public static Messages MESSAGES;

    static {
        LOGGER = Logger.getLogger("commandapi");
        LOGGER.setLevel(Level.WARNING);
    }

    private final LiteralTreeCommand<S> base;
    private final TextComponent prefix;
    private final boolean async;
    private final ExecutorService threadPool;

    public Command(String label, boolean async) {
        TreeCommand<S> baseCommand = this.generateCommand(new LiteralTreeCommandBuilder<>(label)).build(this);
        if (!(baseCommand instanceof LiteralTreeCommand<S> literalTreeCommand)) {
            throw new IllegalArgumentException("You can only use LiteralTreeCommandBuilder as root");
        }
        this.base = literalTreeCommand;
        this.prefix = Texts.subPrefix(Texts.primary(this.base.getName())).append(Component.space());
        this.async = async;
        if (this.async) {
            this.threadPool = Executors.newFixedThreadPool(4);
        } else {
            this.threadPool = null;
        }
    }

    public abstract LiteralTreeCommandBuilder<S> generateCommand(LiteralTreeCommandBuilder<S> builder);

    public abstract boolean hasPermission(S source, String permission);
    public abstract void sendMessage(S source, TextComponent message);

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
            TextComponent message = this.generateHelpMessage(source);
            if (message == null) {
                this.sendMessage(source, Component.text("Missing Exception Message for Exception: " + e.getClass().getSimpleName()));
                return;
            }
            this.sendMessage(source, message);
        } catch (CommandParseException e) {
            this.sendMessage(source, MESSAGES.getMessage(e));
        }
    }

    public void executeRaw(ParsedCommandBranch<S> result, S source) throws CommandParseException {
        Command.LOGGER.info("Command for Execution: " + result.getBranch().stream().map(TreeCommand::getName).toList());
        Command.LOGGER.info("Command for Execution2: " + result.getReader().debugString());
        TreeCommand<S> lastParsed = result.getLastParsedTreeCommand();
        if (result.getException() != null && lastParsed instanceof ParameterTreeCommand<?,?>) {
            throw result.getException();
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
            Command.LOGGER.info("Checking Result: " + result.getBranch().stream().map(TreeCommand::getName).toList());
            StringReader reader = result.getReader();
            String remaining = reader.getRemaining();
            // We only get suggestions if the remaining text starts with a whitespace and the next char is no whitespace
            if (remaining.startsWith(" ") && !remaining.substring(1).startsWith(" ")) {
                reader.skip();
                TreeCommand<S> lastParsedTreeCommand = result.getLastParsedTreeCommand();
                List<TreeCommand<S>> suggestionTreeCommands;
                // If the last command was parsed successfully we get suggestions for the child commands
                if (result.getException() != null) {
                    suggestionTreeCommands = List.of(lastParsedTreeCommand);
                } else {
                    suggestionTreeCommands = lastParsedTreeCommand.getChildren();
                }
                for (TreeCommand<S> suggestionTreeCommand : suggestionTreeCommands) {
                    Command.LOGGER.info("Getting Suggestions for TreeCommand: " + suggestionTreeCommand.getName());
                    List<String> resultSuggestions = suggestionTreeCommand.getSuggestions(result, source);
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
            Command.LOGGER.info(parsedCommandBranch.getBranch().stream().map(TreeCommand::getName).toList() + ": " + parsedCommandBranch.getReader().debugString());
            Command.LOGGER.info("Tree Commands:" + parsedCommandBranch.getBranch().size());
            Command.LOGGER.info("Exception:" + parsedCommandBranch.getException());
            if (parsedCommandBranch.getLastParsedTreeCommand() != null) {
                Command.LOGGER.info("Executor:" + parsedCommandBranch.getLastParsedTreeCommand().getCommandExecutor());
            }
        }
        return results.get(0);
    }

    public TextComponent generateHelpMessage(S source) {
        List<List<TreeCommand<S>>> branches = this.getBase().getBranches(source);
        TextComponent helpMessage = Component.newline()
            .append(Texts.headline(this.getBase().getName().toUpperCase()).append(Component.newline()));
        for (List<TreeCommand<S>> branch : branches) {
            // We only want to generate the branchCommand to the first Parameter
            boolean parameterReached = false;
            StringBuilder branchCommand = new StringBuilder("/");
            TextComponent branchMessage = Texts.primary("/");
            for (int i = 0; i < branch.size(); i++) {
                TreeCommand<S> treeCommand = branch.get(i);
                if (treeCommand instanceof ParameterTreeCommand<?,?>) {
                    parameterReached = true;
                }
                TextComponent treeCommandMessage = treeCommand.getHelpComponent().color(i == 0 ? Colors.PRIMARY : Colors.INFERIOR);
                TextComponent hoverText = Component.empty();
                TextComponent description = treeCommand.getDescription();
                TextComponent extraDescription = treeCommand.getHelpExtraDescription();
                if (description != null) hoverText = hoverText.append(description);
                if (description != null && extraDescription != null) hoverText = hoverText.append(Component.newline());
                if (extraDescription != null) hoverText = hoverText.append(extraDescription);
                if (description != null || extraDescription != null) {
                    treeCommandMessage = treeCommandMessage.hoverEvent(HoverEvent.showText(hoverText));
                }
                branchMessage = branchMessage.append(treeCommandMessage).append(Component.space());
                if (!parameterReached) {
                    branchCommand.append(treeCommand.getName()).append(" ");
                }
            }
            branchMessage = branchMessage.clickEvent(ClickEvent.suggestCommand(branchCommand.toString()));
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }

}
