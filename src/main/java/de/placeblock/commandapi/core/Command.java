package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.parser.StringReader;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Placeblock
 */
@Getter
public abstract class Command<S> {

    private final LiteralTreeCommand<S> base;
    private final TextComponent prefix;

    public Command(String label) {
        TreeCommand<S> baseCommand = this.generateCommand(new LiteralTreeCommandBuilder<>(label)).build(this);
        if (!(baseCommand instanceof LiteralTreeCommand<S> literalTreeCommand)) {
            throw new IllegalArgumentException("You can only use LiteralTreeCommandBuilder as root");
        }
        this.base = literalTreeCommand;
        this.prefix = Texts.subPrefix(Texts.primary(this.base.getName())).append(Component.space());
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
        if (lastParsedCommand != null) {
            Map<TreeCommand<S>, CommandException> errors = context.getErrors();
            for (TreeCommand<S> errTreeCommand : errors.keySet()) {
                if (lastParsedCommand.getChildren().contains(errTreeCommand)) {
                   this.sendMessage(source, errors.get(errTreeCommand).getTextMessage());
                    return;
                }
            }
            if (context.getReader().getRemaining().equals("") && lastParsedCommand.getRun() != null) {
                lastParsedCommand.getRun().accept(context);
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
        if (wrongInformation.equals("") && lastParsedCommand instanceof ParameterTreeCommand<S,?> parameterTreeCommand) {
            return parameterTreeCommand.getSuggestions(context);
        }
        if (wrongInformation.startsWith(" ") && !wrongInformation.substring(1).contains(" ")) {
            List<String> suggestions = new ArrayList<>();
            for (TreeCommand<S> child : lastParsedCommand.getChildren()) {
                suggestions.addAll(child.getSuggestions(context));
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    public TextComponent generateHelpMessage(S source) {
        List<List<TreeCommand<S>>> branches = this.getBase().getBranches(source);
        TextComponent helpMessage = Texts.headline(this.getBase().getName().toUpperCase()).append(Component.newline());
        for (List<TreeCommand<S>> branch : branches) {
            StringBuilder branchCommand;
            TextComponent branchMessage = Texts.primary("/");
            branchMessage = branchMessage.append(Texts.primary(branch.get(0).getName()));
            branchCommand = new StringBuilder("/" + branch.get(0).getName());
            for (int i = 1; i < branch.size(); i++) {
                TreeCommand<S> treeCommand = branch.get(i);
                branchMessage = branchMessage.append(Component.space()).append(treeCommand.getHelpComponent());
                branchCommand.append(" ").append(treeCommand.getName());
            }
            branchMessage = branchMessage.clickEvent(ClickEvent.suggestCommand(branchCommand.toString()));
            TextComponent lastDescription = this.getLastDescription(branch);
            if (lastDescription != null) {
                branchMessage = branchMessage.hoverEvent(HoverEvent.showText(lastDescription));
            }
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }

    public TextComponent getLastDescription(List<TreeCommand<S>> branch) {
        for (TreeCommand<S> treeCommand : branch) {
            if (treeCommand.getDescription() != null) return treeCommand.getDescription();
        }
        return null;
    }

}
