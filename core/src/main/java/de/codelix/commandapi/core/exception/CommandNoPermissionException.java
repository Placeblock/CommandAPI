package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.TreeCommand;

/**
 * Author: Placeblock
 */
public class CommandNoPermissionException extends CommandParseException {
    public CommandNoPermissionException(TreeCommand<?> treeCommand) {
        super();
        this.setTreeCommand(treeCommand);
    }
}
