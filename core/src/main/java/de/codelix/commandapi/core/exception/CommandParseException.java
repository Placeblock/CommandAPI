package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.CommandNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Placeblock
 */
@Setter
@Getter
public class CommandParseException extends CommandException {
    private CommandNode<?> commandNode;
}
