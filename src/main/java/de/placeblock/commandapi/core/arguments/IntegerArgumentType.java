package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;

@SuppressWarnings("unused")
public class IntegerArgumentType<S> implements ArgumentType<S, Integer> {
    private final int minimum;
    private final int maximum;

    private IntegerArgumentType(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static <S> IntegerArgumentType<S> integer() {
        return integer(Integer.MIN_VALUE);
    }

    public static <S> IntegerArgumentType<S> integer(final int min) {
        return integer(min, Integer.MAX_VALUE);
    }

    public static <S> IntegerArgumentType<S> integer(final int min, final int max) {
        return new IntegerArgumentType<S>(min, max);
    }

    public static int getInteger(final CommandContext<?> context, final String name) {
        return context.getArgument(name, int.class);
    }

    @Override
    public Integer parse(S source, StringReader reader) throws CommandException {
        final int start = reader.getCursor();
        final int result = reader.readInt();
        if (result < minimum) {
            reader.setCursor(start);
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+result+" <color:inferior>ist <color:negative>zu klein<color:inferior>. Das Minimum ist <color:negative>" + this.minimum));
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+result+" <color:inferior>ist <color:negative>zu groß<color:inferior>. Das Maximum ist <color:negative>" + this.maximum));
        }
        return result;
    }
}
