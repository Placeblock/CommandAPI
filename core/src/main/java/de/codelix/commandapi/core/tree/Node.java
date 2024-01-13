package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.RunConsumer;
import lombok.NonNull;

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

    /**
     * DisplayName is the name that is shown to the user
     * @return The display name
     */
    String getDisplayName();

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
    void parse(ParseContext<S> ctx, ParsedCommand<S> parsedCommand) throws SyntaxException;

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
