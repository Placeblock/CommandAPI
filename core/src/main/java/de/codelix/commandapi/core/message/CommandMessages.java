package de.codelix.commandapi.core.message;

import de.codelix.commandapi.core.exception.CommandException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CommandMessages<M> {

    private final Map<Class<? extends CommandException>, Function<CommandException, M>> messages = new HashMap<>();

    public <T extends CommandException> void add(Class<T> clazz, Function<T, M> message) {
        this.messages.put(clazz, ex -> message.apply(clazz.cast(ex)));
    }

    public void addAll(CommandMessages<M> messages) {
        this.messages.putAll(messages.messages);
    }

    public <T extends CommandException>  M getMessage(T exception) {
        Function<CommandException, M> generator = this.messages.get(exception.getClass());
        if (generator == null) return null;
        return generator.apply(exception);
    }

}
