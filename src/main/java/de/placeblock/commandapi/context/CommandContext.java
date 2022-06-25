package de.placeblock.commandapi.context;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class CommandContext<S> {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = new HashMap<>();

    static {
        PRIMITIVE_TO_WRAPPER.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER.put(short.class, Short.class);
        PRIMITIVE_TO_WRAPPER.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER.put(long.class, Long.class);
        PRIMITIVE_TO_WRAPPER.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER.put(double.class, Double.class);
    }

    @Getter
    private final S source;
    @Getter
    private final String input;
    @Getter
    private final Command<S> command;
    private final CommandContext<S> child;
    private final Map<String, ParsedArgument<S, ?>> arguments;
    @Getter
    private final StringRange range;

    public CommandContext(S source, String input, CommandContext<S> child, Map<String, ParsedArgument<S, ?>> arguments, Command<S> command, StringRange range) {
        this.source = source;
        this.input = input;
        this.child = child;
        this.arguments = arguments;
        this.command = command;
        this.range = range;
    }

    @SuppressWarnings("unchecked")
    public <V> V getArgument(String name, Class<V> clazz) {
        final ParsedArgument<S, ?> argument = this.arguments.get(name);

        if (argument == null) {
            throw new IllegalArgumentException("No such argument '" + name + "' exists on this command");
        }

        Object result = argument.getResult();
        if (PRIMITIVE_TO_WRAPPER.getOrDefault(clazz, clazz).isAssignableFrom(result.getClass())) {
            return (V) result;
        } else {
            throw new IllegalArgumentException("Argument '" + name + "' is defined as " + result.getClass().getSimpleName() + ", not " + clazz);
        }
    }

    @Override
    public String toString() {
        return "CommandContext{" +
            "source=" + source +
            ", input='" + input + '\'' +
            ", command=" + command +
            ", arguments=" + arguments +
            ", range=" + range +
            '}';
    }

    public CommandContext<S> getChild() {
        return child;
    }
}
