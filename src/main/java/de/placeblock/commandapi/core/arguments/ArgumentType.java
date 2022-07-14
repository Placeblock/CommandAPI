package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;

import java.util.ArrayList;
import java.util.List;

public interface ArgumentType<S, T> {

    T parse(S source, StringReader reader) throws CommandException;

    default List<String> listSuggestions(CommandContext<S> context, String partial) {
        return new ArrayList<>();
    }

}
