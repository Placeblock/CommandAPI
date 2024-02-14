package de.codelix.commandapi.core.parameter.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IntegerTooSmallParseException extends ParseException {
    private final int value;
    private final int min;
}
