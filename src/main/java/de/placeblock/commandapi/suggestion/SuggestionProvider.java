package de.placeblock.commandapi.suggestion;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.exception.CommandSyntaxException;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface SuggestionProvider<S> {
    CompletableFuture<Suggestions> getSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) throws CommandSyntaxException;
}
