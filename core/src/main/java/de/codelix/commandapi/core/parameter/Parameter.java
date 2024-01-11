package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Parameter<D> {

    D parse(ParseContext ctx, ParsedCommand cmd) throws SyntaxException;

    default CompletableFuture<List<D>> getSuggestionsAsync(ParseContext ctx, ParsedCommand cmd) {
        return CompletableFuture.completedFuture(this.getSuggestions(ctx, cmd));
    }

    default List<D> getSuggestions(ParseContext ctx, ParsedCommand cmd) {
        return new ArrayList<>();
    }
}
