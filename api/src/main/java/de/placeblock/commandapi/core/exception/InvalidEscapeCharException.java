package de.placeblock.commandapi.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidEscapeCharException extends CommandParseException {
    private final char character;
}
