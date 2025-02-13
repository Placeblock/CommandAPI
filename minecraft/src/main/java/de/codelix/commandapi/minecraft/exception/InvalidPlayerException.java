package de.codelix.commandapi.minecraft.exception;

import de.codelix.commandapi.core.exception.CommandException;

public class InvalidPlayerException extends RuntimeException implements CommandException {
    public InvalidPlayerException(String message) {
        super(message);
    }
}
