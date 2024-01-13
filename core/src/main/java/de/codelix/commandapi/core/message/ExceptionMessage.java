package de.codelix.commandapi.core.message;

import de.codelix.commandapi.core.exception.SyntaxException;

@FunctionalInterface
public interface ExceptionMessage<E extends SyntaxException, M> {
    M get(Class<E> exception);
}
