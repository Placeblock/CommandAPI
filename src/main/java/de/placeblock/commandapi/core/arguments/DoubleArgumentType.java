package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;

public class DoubleArgumentType<S> implements ArgumentType<S, Double> {
    private final double minimum;
    private final double maximum;

    private DoubleArgumentType(double minimum, double maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public static <S> DoubleArgumentType<S> doubleArg() {
        return doubleArg(Double.MIN_VALUE);
    }

    public static <S> DoubleArgumentType<S> doubleArg(final double min) {
        return doubleArg(min, Double.MAX_VALUE);
    }

    public static <S> DoubleArgumentType<S> doubleArg(final double min, final double max) {
        return new DoubleArgumentType<>(min, max);
    }

    public static double getDouble(final CommandContext<?> context, final String name) {
        return context.getArgument(name, double.class);
    }

    @Override
    public Double parse(S source, StringReader reader) throws CommandException {
        final int start = reader.getCursor();
        final double result = reader.readDouble();
        if (result < this.minimum) {
            reader.setCursor(start);
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+result+" <color:inferior>ist <color:negative>zu klein<color:inferior>. Das Minimum ist <color:negative>" + this.minimum));
        }
        if (result > this.maximum) {
            reader.setCursor(start);
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+result+" <color:inferior>ist <color:negative>zu groß<color:inferior>. Das Maximum ist <color:negative>" + this.maximum));
        }
        return result;
    }
}
