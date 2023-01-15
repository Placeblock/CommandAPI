package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParseContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public abstract class TreeCommand<S> {

    private final Command<S> command;
    private final String name;
    private final List<TreeCommand<S>> children;
    private final TextComponent description;
    private final String permission;
    private final Consumer<ParseContext<S>> run;

    abstract void parse(ParseContext<S> context);

    public void parseRecursive(ParseContext<S> context) {
        int oldcursor = context.getCursor();

        // Check Permissions
        if (this.hasNoPermission(context.getSource())) return;

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

    public List<List<TreeCommand<S>>> getBranches(S source) {
        List<List<TreeCommand<S>>> branches = new ArrayList<>();
        if (this.children.size() == 0) {
            return List.of(List.of(this));
        }
        for (TreeCommand<S> child : this.children) {
            if (child.hasNoPermission(source)) continue;
            List<List<TreeCommand<S>>> childBranches = child.getBranches(source);
            for (List<TreeCommand<S>> childBranch : childBranches) {
                childBranch.add(0, this);
                branches.add(childBranch);
            }
        }
        return branches;
    }

    public boolean hasNoPermission(S source) {
        if (this.permission == null) return false;
        return !this.command.hasPermission(source, this.permission);
    }

    public abstract List<String> getSuggestions(ParseContext<S> context);
}
