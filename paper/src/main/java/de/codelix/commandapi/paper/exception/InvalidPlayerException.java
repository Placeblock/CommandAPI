package de.codelix.commandapi.paper.exception;

import de.codelix.commandapi.core.exception.CommandParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidPlayerException extends CommandParseException {
    private final String name;
}
