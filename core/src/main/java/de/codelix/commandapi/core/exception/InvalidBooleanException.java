package de.codelix.commandapi.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidBooleanException extends CommandParseException{
    private final String bool;
}
