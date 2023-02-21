package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;
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

    public List<ParsedCommand<S>> parse(String text, S source) {
        ParsedCommand<S> parsedCommand = new ParsedCommand<>(new StringReader(text));
        return this.base.parseRecursive(parsedCommand, source);
    }

    public void execute(ParsedCommand<S> result, S source) throws CommandSyntaxException {
        if (result.getReader().canRead()) {
            if (result.getExceptions().size() >= 1) {
                throw result.getExceptions().values().iterator().next();
            } else {
                this.sendMessage(source, this.generateHelpMessage(source));
                return;
            }
        }
        List<TreeCommand<S>> parsedCommands = result.getParsedTreeCommands();
        CommandExecutor<S> commandExecutor = parsedCommands.get(parsedCommands.size() - 1).getCommandExecutor();
        if (commandExecutor == null) {
            this.sendMessage(source, this.generateHelpMessage(source));
        } else {
            commandExecutor.run(result, source);
        }
    }

    public List<String> getSuggestions(List<ParsedCommand<S>> results, S source) {
        List<String> suggestions = new ArrayList<>();
        for (ParsedCommand<S> result : results) {
            TreeCommand<S> lastParsedTreeCommand = result.getLastParsedTreeCommand();
            List<String> resultSuggestions = lastParsedTreeCommand.getSuggestions(result, source);
            suggestions.addAll(resultSuggestions);
        }
        return suggestions;
    }

    public static <S> ParsedCommand<S> getBestResult(List<ParsedCommand<S>> results, S source) {
        results.sort((a, b) -> {
            TreeCommand<S> aLastParsedTreeCommand = a.getLastParsedTreeCommand();
            TreeCommand<S> bLastParsedTreeCommand = b.getLastParsedTreeCommand();
            int aShortestBranchLength = aLastParsedTreeCommand.getShortestBranchLength(source);
            int bShortestBranchLength = bLastParsedTreeCommand.getShortestBranchLength(source);
            if (aShortestBranchLength < bShortestBranchLength) return -1;
            if (aShortestBranchLength > bShortestBranchLength) return 1;
            if (a.getReader().canRead() && !b.getReader().canRead()) return -1;
            if (!a.getReader().canRead() && b.getReader().canRead()) return 1;
            if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) return -1;
            if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) return 1;
            return 0;
        });
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
