package de.placeblock.commandapi.core.messages;

import de.placeblock.commandapi.core.exception.CommandParseException;
import net.kyori.adventure.text.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Messages {
    private final Map<Class<? extends CommandParseException>, Function<CommandParseException, TextComponent>> messages = new HashMap<>();

    public <T extends CommandParseException> void register(Class<T> clazz, Function<T, TextComponent> message) {
        this.messages.put(clazz, ex -> message.apply(clazz.cast(message)));
    }

    public <T extends CommandParseException>  TextComponent getMessage(T exception) {
        return this.messages.get(exception.getClass()).apply(exception);
    }
}
