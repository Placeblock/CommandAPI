package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Literal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidLiteralParseException extends ParseException {
    private final Literal<?, ?> node;
    private final String provided;
}
