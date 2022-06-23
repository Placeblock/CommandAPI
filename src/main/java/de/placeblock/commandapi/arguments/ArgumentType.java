package de.placeblock.commandapi.arguments;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.suggestion.Suggestions;
import de.placeblock.commandapi.suggestion.SuggestionsBuilder;
import de.placeblock.commandapi.util.StringReader;

import java.util.concurrent.CompletableFuture;

public interface ArgumentType<T> {

    T parse(final StringReader reader) throws CommandSyntaxException;

    default <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

}
