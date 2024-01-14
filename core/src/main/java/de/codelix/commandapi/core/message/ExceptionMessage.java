package de.codelix.commandapi.core.message;

import de.codelix.commandapi.core.exception.ParseException;

@FunctionalInterface
public interface ExceptionMessage<E extends ParseException, M> {
    M get(Class<E> exception);
}
