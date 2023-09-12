package de.codelix.commandapi.core.messages;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.exception.CommandParseException;
import net.kyori.adventure.text.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class CommandDesign {
    private final Map<Class<? extends CommandParseException>, Function<CommandParseException, TextComponent>> messages = new HashMap<>();

    public <T extends CommandParseException> void register(Class<T> clazz, Function<T, TextComponent> message) {
        this.messages.put(clazz, ex -> message.apply(clazz.cast(ex)));
    }

    public <T extends CommandParseException>  TextComponent getMessage(T exception) {
        Function<CommandParseException, TextComponent> generator = this.messages.get(exception.getClass());
        if (generator == null) return null;
        return generator.apply(exception);
    }

    public abstract TextComponent getPrefix(Command<?> commandName);

    public abstract <S> TextComponent generateHelpMessage(Command<S> command, S source);
}
