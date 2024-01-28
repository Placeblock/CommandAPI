package de.codelix.commandapi.core.parameter.exceptions;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.impl.BooleanParameter;
import lombok.Getter;

@Getter
public class InvalidBooleanParseException extends ParseException {
    private final BooleanParameter<?, ?> parameter;
    private final String input;

    public InvalidBooleanParseException(BooleanParameter<?, ?> parameter, String input) {
        this.parameter = parameter;
        this.input = input;
    }
}
