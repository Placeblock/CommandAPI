package de.codelix.commandapi.core.exception;

import lombok.Getter;

@Getter
public class NumberTooBigException extends CommandParseException {
    private final Number number;
    private final Number max;

    public <T extends Number> NumberTooBigException(T value, T max) {
        this.number = value;
        this.max = max;
    }

}
