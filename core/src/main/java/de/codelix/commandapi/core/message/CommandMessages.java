package de.codelix.commandapi.core.message;

import de.codelix.commandapi.core.exception.SyntaxException;

import java.util.HashMap;
import java.util.Map;

public class CommandMessages<M> {

    Map<Class<SyntaxException>, ExceptionMessage<?, M>> messages = new HashMap<>();

    public M getMessage(SyntaxException ex) {
        ExceptionMessage<?, M> exceptionMessage = this.messages.get(ex.getClass());
        if (exceptionMessage == null) return null;
        return exceptionMessage.get(ex);
    }

}
