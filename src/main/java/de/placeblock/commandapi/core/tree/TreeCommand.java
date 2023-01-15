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

        // Parse the current Command
        this.parse(context);

        // Stop if nothing has changed = couldn't parse
        if (context.getCursor() <= oldcursor) return;

        // Skip whitespace
        context.setCursor(context.getCursor() + 1);

        // Only move forward if we haven't reached the end already
        if (context.getCursor() >= context.getText().length()) return;

        // Parse Children
        oldcursor = context.getCursor();
        for (TreeCommand<S> child : this.children) {
            child.parseRecursive(context);
            //Break if one child was successful
            if (context.getCursor() > oldcursor) break;
        }
    }

    public abstract List<String> getSuggestions(ParseContext<S> context);

    public void print(int round) {
        System.out.println(" ".repeat(round*5) + this.getName());
        System.out.println(" ".repeat(round*5) + this.getDescription());
        System.out.println(" ".repeat(round*5) + this.getRun());
        System.out.println(" ".repeat(round*5) + this.getPermissions());
        System.out.println(" ".repeat(round*5) + this.getChildren().size());
        for (TreeCommand<S> child : this.children) {
            child.print(round + 1);
        }
    }
}
