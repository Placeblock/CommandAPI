package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;

@Getter
public class InvalidArgumentSyntaxException extends SyntaxException {
    private final Argument<?, ?> node;
    public InvalidArgumentSyntaxException(Argument<?, ?> node) {
        this.node = node;
    }
}
