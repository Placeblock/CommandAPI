package de.placeblock.commandapi.core.exception;

import io.schark.design.texts.Texts;

public class CommandNoPermissionException extends CommandException {

    public CommandNoPermissionException() {
        super(Texts.INSUFFICIENT_PERMISSIONS);
    }

}
