package de.placeblock.commandapi.arguments;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringReader;

public class IntegerArgumentType implements ArgumentType<Integer> {
    private final int minimum;
    private final int maximum;

    public IntegerArgumentType(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static int getInteger(final CommandContext<?> context, final String name) {
        return context.getArgument(name, int.class);
    }

    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final int result = reader.readInt();
        if (result < minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, result, minimum);
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, maximum);
        }
        return result;
    }
}
