package de.placeblock.commandapi.core.exception;

import io.schark.design.Texts;

public class CommandNoPermissionException extends CommandException {

    public CommandNoPermissionException() {
        super(Texts.inferior("Dazu hast du <color:negative>keine Berechtigung"));
    }

}
