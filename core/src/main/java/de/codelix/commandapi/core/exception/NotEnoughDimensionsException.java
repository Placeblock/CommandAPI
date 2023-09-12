package de.codelix.commandapi.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotEnoughDimensionsException extends CommandParseException {
    private final int provided;
    private final int dimensions;
}
