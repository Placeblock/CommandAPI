package de.placeblock.commandapi.core.messages;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import net.kyori.adventure.text.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Messages {

    private final Map<Class<? extends CommandSyntaxException>, Function<? extends CommandSyntaxException, TextComponent>> messages = new HashMap<>();

    public <T extends CommandSyntaxException> void put(Class<T> clazz, Function<T, TextComponent> message) {
        this.messages.put(clazz, message);
    }

    public <T extends CommandSyntaxException> TextComponent getMessage(T exception) {
        Function<? extends CommandSyntaxException, TextComponent> textComponentFunction = this.messages.get(exception.getClass());
        return textComponentFunction.apply(exception);
    }

}
