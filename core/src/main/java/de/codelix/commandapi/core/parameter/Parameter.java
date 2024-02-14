package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Parameter<T, S extends Source<M>, M> {

    T parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) throws ParseException;

    default CompletableFuture<List<String>> getSuggestionsAsync(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        return CompletableFuture.completedFuture(this.getSuggestions(ctx, cmd));
    }

    default List<String> getSuggestions(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        return new ArrayList<>();
    }

    default List<String> startsWith(Collection<String> list, String partial) {
        return list.stream().filter(item -> item.startsWith(partial)).toList();
    }
}
