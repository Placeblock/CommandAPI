package de.placeblock.commandapi.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidParameterValueException extends CommandParseException{
    private final String parameter;
}
