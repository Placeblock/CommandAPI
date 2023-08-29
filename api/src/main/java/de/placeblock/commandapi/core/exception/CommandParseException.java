package de.placeblock.commandapi.core.exception;

import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.Setter;

/**
 * Author: Placeblock
 */
@Setter
@Getter
public class CommandParseException extends CommandException {
    private TreeCommand<?> treeCommand;
}
