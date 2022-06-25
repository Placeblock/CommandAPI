package de.placeblock.commandapi.arguments;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArgumentType<T> {

    T parse(final StringReader reader) throws CommandSyntaxException;

    default <S> CompletableFuture<List<String>> listSuggestions(final CommandContext<S> context, String partial) {
        return CompletableFuture.completedFuture(new ArrayList<>());
    }

}
