package de.placeblock.commandapi.core.exception;

import io.schark.design.texts.Texts;

/**
 * Author: Placeblock
 */
public class CommandNoPermissionException extends CommandException{
    public CommandNoPermissionException() {
        super(Texts.INSUFFICIENT_PERMISSIONS);
    }
}
