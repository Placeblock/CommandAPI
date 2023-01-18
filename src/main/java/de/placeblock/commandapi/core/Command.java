package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
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
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Placeblock
 */
@Getter
public abstract class Command<S> {

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

    public ParseContext<S> parse(String text, S source) {
        ParseContext<S> parseContext = new ParseContext<>(new StringReader(text), source);
        this.base.parseRecursive(parseContext);
        return parseContext;
    }

    public void execute(ParseContext<S> context) {
        S source = context.getSource();
        TreeCommand<S> lastParsedCommand = context.getLastParsedCommand();
        // If the last parsed command is null we can skip the proccess
        if (lastParsedCommand != null) {
            // We have to check Errors
            Map<String, ParsedValue<?>> errors = context.getParameters();
            List<String> lastParsedCommandChildrenNames = lastParsedCommand.getChildren().stream().map(TreeCommand::getName).toList();
            for (String parameterName : errors.keySet()) {
                // Only Errors at children of the last parsed command are important
                if (lastParsedCommandChildrenNames.contains(parameterName)) {
                    CommandSyntaxException syntaxException = errors.get(parameterName).getSyntaxException();
                    if (syntaxException != null) {
                        this.sendMessage(source, syntaxException.getTextMessage());
                        return;
                    }
                }
            }
            if (context.getReader().getRemaining().equals("") && lastParsedCommand.getRun() != null) {
                if (this.isAsync()) {
                    this.threadPool.execute(() -> lastParsedCommand.getRun().accept(context));
                } else {
                    lastParsedCommand.getRun().accept(context);
                }
                return;
            }
        }
        this.sendMessage(source, this.generateHelpMessage(source));
    }

    public List<String> getSuggestions(ParseContext<S> context) {
        TreeCommand<S> lastParsedCommand = context.getLastParsedCommand();
        if (lastParsedCommand == null) {
            return this.base.getSuggestions(context);
        }
        String wrongInformation = context.getReader().getRemaining();

        // We want to display autocompletion of parameters even if they are valid
        if (wrongInformation.equals("") && lastParsedCommand instanceof ParameterTreeCommand<S,?> parameterTreeCommand) {
            return parameterTreeCommand.getSuggestions(context);
        }

        if (wrongInformation.startsWith(" ") && !wrongInformation.substring(1).contains(" ")) {
            List<String> suggestions = new ArrayList<>();
            context.getReader().skip();
            for (TreeCommand<S> child : lastParsedCommand.getChildren()) {
                int cursor = context.getReader().getCursor();
                suggestions.addAll(child.getSuggestions(context));
                context.getReader().setCursor(cursor);
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    public TextComponent generateHelpMessage(S source) {
        List<List<TreeCommand<S>>> branches = this.getBase().getBranches(source);
        TextComponent helpMessage = Component.newline()
            .append(Texts.headline(this.getBase().getName().toUpperCase()).append(Component.newline()));
        for (List<TreeCommand<S>> branch : branches) {
            StringBuilder branchCommand = new StringBuilder("/");
            TextComponent branchMessage = Texts.primary("/");
            for (int i = 0; i < branch.size(); i++) {
                TreeCommand<S> treeCommand = branch.get(i);
                TextComponent treeCommandMessage = treeCommand.getHelpComponent().color(i == 0 ? Colors.PRIMARY : Colors.INFERIOR);
                TextComponent description = treeCommand.getDescription();
                TextComponent extraDescription = treeCommand.getHelpExtraDescription();
                if (extraDescription != null) description = description.append(Component.newline()).append(extraDescription);
                treeCommandMessage = treeCommandMessage.hoverEvent(HoverEvent.showText(description));
                branchMessage = branchMessage.append(treeCommandMessage).append(Component.space());
                branchCommand.append(treeCommand.getName()).append(" ");
            }
            branchMessage = branchMessage.clickEvent(ClickEvent.suggestCommand(branchCommand.toString()));
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }

}
