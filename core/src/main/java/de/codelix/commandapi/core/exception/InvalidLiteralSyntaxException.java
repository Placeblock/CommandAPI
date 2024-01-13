package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Literal;
import lombok.Getter;

@Getter
public class InvalidLiteralSyntaxException extends SyntaxException {
    private final Literal<?> node;
    public InvalidLiteralSyntaxException(Literal<?> node) {
        this.node = node;
    }
}
