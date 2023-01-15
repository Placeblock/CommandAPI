package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.parser.ParseContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public abstract class TreeCommand<S> {

    private final String name;
    private final List<TreeCommand<S>> children;
    private final TextComponent description;
    private final List<String> permissions;
    private final Consumer<S> run;

    abstract void parse(ParseContext<S> context);

    public void parseRecursive(ParseContext<S> context) {
        int oldcursor = context.getCursor();
        this.parse(context);
        if (context.getCursor() > oldcursor) {
            context.setCursor(context.getCursor() + 1);
            oldcursor = context.getCursor();
            for (TreeCommand<S> child : this.children) {
                child.parse(context);
                if (context.getCursor() > oldcursor) break;
            }
        }
    }

    public abstract List<String> getSuggestions(ParseContext<S> context);

}
