package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Node;

public class InvalidLiteralSyntaxException extends SyntaxException {
    public InvalidLiteralSyntaxException(Node<?> node) {
        super(node);
    }
}
