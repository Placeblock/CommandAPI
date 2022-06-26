package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;

@SuppressWarnings("unused")
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
    public Integer parse(StringReader reader) throws CommandException {
        final int start = reader.getCursor();
        final int result = reader.readInt();
        if (result < minimum) {
            reader.setCursor(start);
            throw new CommandSyntaxException(Texts.secondary("Die angegebene Zahl <color:negative>"+result+" <color:secondary>ist <color:negative>zu klein<color:secondary>. Das Minimum ist <color:negative>" + this.minimum));
        }
        if (result > maximum) {
            reader.setCursor(start);
            throw new CommandSyntaxException(Texts.secondary("Die angegebene Zahl <color:negative>"+result+" <color:secondary>ist <color:negative>zu groß<color:secondary>. Das Maximum ist <color:negative>" + this.minimum));
        }
        return result;
    }
}
