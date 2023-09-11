package de.codelix.commandapi.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidDecimalException extends CommandParseException {
    private final String decimal;
}
