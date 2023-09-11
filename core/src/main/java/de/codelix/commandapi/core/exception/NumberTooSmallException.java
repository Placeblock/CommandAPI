package de.codelix.commandapi.core.exception;

import lombok.Getter;

@Getter
public class NumberTooSmallException extends CommandParseException {
    private final Number number;
    private final Number min;

    public <T extends Number> NumberTooSmallException(T value, T min) {
        this.number = value;
        this.min = min;
    }
}
