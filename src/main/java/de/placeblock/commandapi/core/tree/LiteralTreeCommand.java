package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.Util;
import de.placeblock.commandapi.core.parser.ParseContext;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
public class LiteralTreeCommand<S> extends TreeCommand<S> {

    public LiteralTreeCommand(Command<S> command, String name, List<TreeCommand<S>> children, TextComponent description,
                              String permission, Consumer<ParseContext<S>> run) {
        super(command, name, children, description, permission, run);
    }

    @Override
    void parse(ParseContext<S> context) {
        if (context.getText().length() < context.getCursor()+this.getName().length()) return;
        int nextWordIndex = Util.readWord(context);
        if (nextWordIndex == 0) return;
        String nextWord = context.getText().substring(context.getCursor(), nextWordIndex);
        if (nextWord.equalsIgnoreCase(this.getName())) {
            context.setCursor(nextWordIndex);
            context.setLastParsedCommand(this);
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        if (this.getName().startsWith(context.getText().substring(context.getCursor()).trim())) {
            return List.of(this.getName());
        } else {
            return new ArrayList<>();
        }
    }
}
