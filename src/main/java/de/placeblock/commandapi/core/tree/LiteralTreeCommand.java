package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.util.Util;
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
        if (context.getText().length() < context.getCursor()+this.getName().length()) {
            return false;
        }
        int nextWordIndex = Util.readWord(context);
        if (nextWordIndex == 0) return false;
        String nextWord = context.getText().substring(context.getCursor(), nextWordIndex);
        if (nextWord.equalsIgnoreCase(this.getName())) {
            context.setCursor(nextWordIndex);
            context.setLastParsedCommand(this);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        if (this.getName().startsWith(context.getText().substring(context.getCursor()).trim())) {
            return List.of(this.getName());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public TextComponent getHelpComponent() {
        return Texts.inferior(this.getName());
    }
}
