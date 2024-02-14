package de.codelix.commandapi.core.parameter.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidDoubleParseException extends ParseException {
    private final String input;
}
