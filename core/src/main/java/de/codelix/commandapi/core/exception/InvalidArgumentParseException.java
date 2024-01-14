package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidArgumentParseException extends ParseException {
    private final Argument<?, ?> node;
}
