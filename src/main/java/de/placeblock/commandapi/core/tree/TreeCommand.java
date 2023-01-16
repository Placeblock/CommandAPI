package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.exception.CommandNoPermissionException;
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

    abstract boolean parse(ParseContext<S> context);

    public boolean parseRecursive(ParseContext<S> context) {
        // Check Permissions
        if (this.hasNoPermission(context.getSource())) {
            context.addError(this, new CommandNoPermissionException());
            return false;
        }

        // Parse the current Command
        if (!this.parse(context)) return false;
        context.addParsedCommand(this);

        // Only move forward if we haven't reached the end already
        if (context.getReader().canRead(1)) return true;

        // Parse Children
        for (TreeCommand<S> child : this.children) {
            int oldcursor = context.getReader().getCursor();

            // Skip white space
            context.getReader().skip();

            if (child.parseRecursive(context)) {
                return true;
            } else {
                context.getReader().setCursor(oldcursor);
            }
        }
        return true;
    }

    public List<List<TreeCommand<S>>> getBranches(S source) {
        List<List<TreeCommand<S>>> branches = new ArrayList<>();
        if (this.children.size() == 0) {
            return new ArrayList<>(List.of(new ArrayList<>(List.of(this))));
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

    public abstract TextComponent getHelpComponent();
}
