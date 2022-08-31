package de.placeblock.commandapi.core.exception;

import io.schark.design.texts.Texts;

public class InvalidCommandException extends CommandException {

    public InvalidCommandException() {
        super(Texts.inferior("Dieser Befehl <color:negative>existiert nicht"));
    }

}
