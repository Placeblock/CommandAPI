package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArgumentType<T> {

    T parse(final StringReader reader) throws CommandException;

    default <S> CompletableFuture<List<String>> listSuggestions(final CommandContext<S> context, String partial) {
        return CompletableFuture.completedFuture(new ArrayList<>());
    }

}
