package de.codelix.commandapi.core.parameter.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DoubleTooLargeParseException extends ParseException {
    private final double value;
    private final double max;
}
