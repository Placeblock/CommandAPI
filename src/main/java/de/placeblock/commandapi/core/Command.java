package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import de.placeblock.commandapi.core.tree.builder.TreeCommandBuilder;
import io.schark.design.texts.Texts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public abstract class Command<S> {

    private final LiteralTreeCommand<S> base;
    private final TextComponent prefix;

    public Command() {
        TreeCommand<S> baseCommand = this.getCommandBuilder().build(this);
        if (!(baseCommand instanceof LiteralTreeCommand<S> literalTreeCommand)) {
            throw new IllegalArgumentException("You can only use LiteralTreeCommandBuilder as root");
        }
        this.base = literalTreeCommand;
        this.prefix = Texts.subPrefix(Texts.primary(this.base.getName())).append(Component.space());
    }

    public abstract TreeCommandBuilder<S> getCommandBuilder();

    public abstract boolean hasPermission(S source, String permission);
    public abstract void sendMessage(S source, TextComponent message);

    public ParseContext<S> parse(String text, S source) {
        ParseContext<S> parseContext = new ParseContext<>(text, source);
        this.base.parseRecursive(parseContext);
        return parseContext;
    }

    public void execute(ParseContext<S> context) {
        if (context.getError() != null) {
            this.sendMessage(context.getSource(), context.getError());
            return;
        }
        if (context.getCursor() > context.getText().length()
            || context.getText().substring(context.getCursor()).trim().equals("")) {
            context.getLastParsedCommand().getRun().accept(context);
        } else {
            this.sendMessage(context.getSource(), this.generateHelpMessage(context.getSource()));
        }
    }

    public List<String> getSuggestions(ParseContext<S> parseContext) {
        String text = parseContext.getText();
        if (parseContext.getCursor() > text.length()
            || parseContext.getError() != null) return new ArrayList<>();
        String wrongInformation = text.substring(parseContext.getCursor());
        if (!wrongInformation.contains(" ")) {
            TreeCommand<S> lastParsedCommand = parseContext.getLastParsedCommand();
            if (lastParsedCommand == null) {
                return this.base.getSuggestions(parseContext);
            }
            List<String> suggestions = new ArrayList<>();
            for (TreeCommand<S> child : lastParsedCommand.getChildren()) {
                suggestions.addAll(child.getSuggestions(parseContext));
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

    public TextComponent generateHelpMessage(S source) {
        List<List<TreeCommand<S>>> branches = this.getBase().getBranches(source);
        TextComponent helpMessage = Texts.headline(this.getBase().getName().toUpperCase());
        for (List<TreeCommand<S>> branch : branches) {
            TextComponent branchMessage = Texts.primary("/");
            branchMessage = branchMessage.append(Texts.primary(branch.get(0).getName()));
            for (int i = 1; i < branch.size(); i++) {
                branchMessage = branchMessage.append(Texts.inferior(" " + branch.get(i).getName()));
            }
            helpMessage = helpMessage.append(branchMessage.append(Component.newline()));
        }
        return helpMessage;
    }

}
