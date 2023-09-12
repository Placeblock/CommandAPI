package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.CommandNode;

/**
 * Author: Placeblock
 */
public class CommandNoPermissionException extends CommandParseException {
    public CommandNoPermissionException(CommandNode<?> commandNode) {
        super();
        this.setCommandNode(commandNode);
    }
}
