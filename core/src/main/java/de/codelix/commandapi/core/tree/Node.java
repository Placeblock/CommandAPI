package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parser.PermissionChecker;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A node represents one element of a command
 */
public interface Node<S> {

    /**
     * Children are sub-elements of a node
     * @return Children of this node
     */
    List<Node<S>> getChildren();

    default List<Node<S>> getChildrenOptional() {
        List<Node<S>> children = new ArrayList<>();
        for (Node<S> child : this.getChildren()) {
            children.add(child);
            if (child.isOptional()) {
               children.addAll(child.getChildrenOptional());
            }
        }
        return children;
    }

    default List<List<Node<S>>> flatten(S source, PermissionChecker<S> permissionChecker) {
        List<List<Node<S>>> branches = new ArrayList<>();
        if (!permissionChecker.hasPermission(source, this.getPermission())) {
            return branches;
        }
        List<Node<S>> children = this.getChildrenOptional();
        if (children.size() == 0 || !this.getRunConsumers().isEmpty()) {
            branches.add(new ArrayList<>(List.of(this)));
        }
        for (Node<S> node : children) {
            List<List<Node<S>>> childBranches = node.flatten(source, permissionChecker);
            for (List<Node<S>> childBranch : childBranches) {
                childBranch.add(0, this);
                branches.add(childBranch);
            }
        }
        return branches;
    }

    /**
     * DisplayName is the name that is shown to the user
     * @return The display name
     */
    String getDisplayName();

    /**
     * The description of the Node. Can be used in for example help messages
     * @return The description
     */
    String getDescription();

    /**
     * DisplayName is the name that is shown to the user.
     * @return The display name
     */
    @NonNull String getDisplayNameSafe();

    /**
     * Indicates whether this argument can be skipped
     * @return true if skipping is allowed
     */
    boolean isOptional();

    /**
     * Consumers that run if this command is executed
     * @return all consumers that run if this command is executed
     */
    Collection<RunConsumer<S>> getRunConsumers();

    /**
     * Parses this node into a branch.
     * @param parsedCommand The branch
     */
    void parse(ParseContext<S> ctx, ParsedCommand<S> parsedCommand) throws ParseException;

    /**
     * Parses this node recursive. It tries to parse everything it can and creates a new branch for every child
     * @param parsedCommand The current branch
     */
    void parseRecursive(ParseContext<S> ctx, ParsedCommand<S> parsedCommand);

    /**
     * The permission that is required to access that Node
     * @return The permission as a string
     */
    String getPermission();

    /**
     * Permissions usually block the whole underlying tree.
     * If this is true it should only block if this specific node is executed.
     * @return true or false xD
     */
    boolean isUnsafePermission();

    CompletableFuture<List<String>> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd);
}
