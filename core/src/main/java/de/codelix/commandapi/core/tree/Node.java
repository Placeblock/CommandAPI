package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.Branch;
import de.codelix.commandapi.core.Permission;

import java.util.List;

/**
 * A node represents one element of a command
 */
public interface Node {

    /**
     * Children are sub-elements of a node
     * @return Children of this node
     */
    List<Node> getChildren();

    /**
     * DisplayName is the name that is shown to the user
     * @return The display name
     */
    String getDisplayName();

    /**
     * Parses this node into a branch
     * @param branch The branch
     */
    void parse(Branch branch);

    /**
     * Parses this node recursive. It tries to parse everything it can and creates a new branch for every children
     * @param branch The current branch
     * @return All branches that were created while parsing
     */
    List<Branch> parseRecursive(Branch branch);

    /**
     * The permission that is required to access that Node
     * @return The permission as a string
     */
    Permission getPermission();

}
