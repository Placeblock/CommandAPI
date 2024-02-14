package de.codelix.commandapi.core.parameter.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.impl.QuotedParameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MarkMissingParseException extends ParseException {

    private final QuotedParameter<?, ?> parameter;
    private final String input;

}
