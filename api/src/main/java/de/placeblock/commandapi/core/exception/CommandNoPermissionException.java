package de.placeblock.commandapi.core.exception;

import de.placeblock.commandapi.core.tree.TreeCommand;

/**
 * Author: Placeblock
 */
public class CommandNoPermissionException extends CommandParseException {
    public CommandNoPermissionException(TreeCommand<?> treeCommand) {
        super();
        this.setTreeCommand(treeCommand);
    }
}
