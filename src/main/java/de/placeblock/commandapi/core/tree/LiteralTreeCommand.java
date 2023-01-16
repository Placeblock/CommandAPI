package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParseContext;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
public class LiteralTreeCommand<S> extends TreeCommand<S> {
    private final List<String> aliases;

    public LiteralTreeCommand(Command<S> command, String name, List<TreeCommand<S>> children, TextComponent description,
                              String permission, Consumer<ParseContext<S>> run, List<String> aliases) {
        super(command, name, children, description, permission, run);
        this.aliases = aliases;
    }

    @Override
    boolean parse(ParseContext<S> context) {
        int remainingLength = context.getReader().getRemainingLength();
        if (remainingLength < this.getName().length() &&
            !this.aliases.stream().map(alias -> remainingLength < alias.length()).toList().contains(false)) {
            return false;
        }
        String nextWord = context.getReader().readUnquotedString();
        return nextWord.equalsIgnoreCase(this.getName()) ||
            this.aliases.stream().map(alias -> alias.equalsIgnoreCase(nextWord)).toList().size() > 0;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        if (!this.getName().startsWith(context.getReader().getRemaining().trim())
            || this.hasNoPermission(context.getSource())) {
            return new ArrayList<>();
        }
        List<String> suggestions = new ArrayList<>(this.aliases);
        suggestions.add(this.getName());
        return suggestions;
    }

    @Override
    public TextComponent getHelpComponent() {
        return Texts.inferior(this.getName());
    }

    @Override
    public TextComponent getHelpExtraDescription() {
        if (this.aliases.size() == 0) return null;
        return Texts.inferior("Alias: " + String.join(", ", this.aliases));
    }
}
