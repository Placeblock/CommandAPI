package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Parameter<T, S> {

    T parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException;

    default CompletableFuture<List<String>> getSuggestionsAsync(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        return CompletableFuture.completedFuture(this.getSuggestions(ctx, cmd));
    }

    default List<String> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        return new ArrayList<>();
    }
}
