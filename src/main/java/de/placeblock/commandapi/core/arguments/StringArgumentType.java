package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;
import lombok.Getter;

@SuppressWarnings("unused")
public class StringArgumentType<S> implements ArgumentType<S, String> {
    @Getter
    private final StringType type;

    private StringArgumentType(StringType type) {
        this.type = type;
    }

    public static <S> StringArgumentType<S> word() {
        return new StringArgumentType<S>(StringType.SINGLE_WORD);
    }

    public static <S> StringArgumentType<S> string() {
        return new StringArgumentType<S>(StringType.QUOTABLE_PHRASE);
    }

    public static <S> StringArgumentType<S> greedyString() {
        return new StringArgumentType<S>(StringType.GREEDY_PHRASE);
    }

    public static String getString(final CommandContext<?> context, final String name) {
        return context.getArgument(name, String.class);
    }

    @Override
    public String parse(S source, final StringReader reader) throws CommandException {
        if (type == StringType.GREEDY_PHRASE) {
            final String text = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            return text;
        } else if (type == StringType.SINGLE_WORD) {
            return reader.readUnquotedString();
        } else {
            return reader.readString();
        }
    }
}
