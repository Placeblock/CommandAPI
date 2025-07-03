package de.codelix.commandapi.minecraft.exception;

import de.codelix.commandapi.core.exception.ParseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidPlayerParseException extends ParseException {
    private final String input;
}
