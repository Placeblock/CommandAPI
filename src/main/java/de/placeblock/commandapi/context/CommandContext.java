package de.placeblock.commandapi.context;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
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
    private final Map<String, ParsedArgument<S, ?>> arguments;
    @Getter
    private final List<ParsedCommandNode<S>> nodes;
    @Getter
    private final CommandContext<S> child;
    @Getter
    private final StringRange range;

    public CommandContext(S source, String input, Map<String, ParsedArgument<S, ?>> arguments, Command<S> command, List<ParsedCommandNode<S>> nodes, StringRange range, CommandContext<S> child) {
        this.source = source;
        this.input = input;
        this.arguments = arguments;
        this.command = command;
        this.nodes = nodes;
        this.range = range;
        this.child = child;
    }

    public boolean hasNodes() {
        return !this.nodes.isEmpty();
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
}
