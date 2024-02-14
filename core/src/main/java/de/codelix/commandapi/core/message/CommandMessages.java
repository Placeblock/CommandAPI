package de.codelix.commandapi.core.message;

import de.codelix.commandapi.core.exception.ParseException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandMessages<M> {

    private final Map<Class<? extends ParseException>, Function<ParseException, M>> messages = new HashMap<>();

    public <T extends ParseException> void add(Class<T> clazz, Function<T, M> message) {
        this.messages.put(clazz, ex -> message.apply(clazz.cast(ex)));
    }

    public void addAll(CommandMessages<M> messages) {
        this.messages.putAll(messages.messages);
    }

    public <T extends ParseException>  M getMessage(T exception) {
        Function<ParseException, M> generator = this.messages.get(exception.getClass());
        if (generator == null) return null;
        return generator.apply(exception);
    }

}
