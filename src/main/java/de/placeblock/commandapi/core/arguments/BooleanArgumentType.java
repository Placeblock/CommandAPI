package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class BooleanArgumentType<S> implements ArgumentType<S, Boolean> {
    @Override
    public Boolean parse(S source, StringReader reader) throws CommandException {
        return reader.readBoolean();
    }

    private BooleanArgumentType() {

    }

    public static <S> BooleanArgumentType<S> bool() {
        return new BooleanArgumentType<>();
    }

    public static boolean getBoolean(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Boolean.class);
    }

    @Override
    public List<String> listSuggestions(CommandContext<S> context, String partial) {
        List<String> completions = new ArrayList<>();
        if ("true".startsWith(partial.toLowerCase())) {
            completions.add("true");
        }
        if ("false".startsWith(partial.toLowerCase())) {
            completions.add("false");
        }
        return completions;
    }
}
