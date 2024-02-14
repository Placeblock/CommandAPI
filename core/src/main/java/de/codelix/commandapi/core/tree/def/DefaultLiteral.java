package de.codelix.commandapi.core.tree.def;

import de.codelix.commandapi.core.exception.InvalidLiteralParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Literal;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DefaultLiteral<S extends Source<M>, M> extends DefaultNode<S, M>, Literal<S, M> {
    @Override
    default void parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        if (!this.getNames().contains(next)) {
            throw new InvalidLiteralParseException(this, next);
        }
    }

    @Override
    default CompletableFuture<List<String>> getSuggestions(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        String next = ctx.getInput().poll();
        assert next != null;
        List<String> suggestions = this.getNames().stream().filter(n -> n.startsWith(next)).toList();
        return CompletableFuture.completedFuture(suggestions);
    }

    @Override
    default @NonNull String getDisplayNameSafe() {
        String displayName = this.getDisplayName();
        if (displayName != null) return displayName;
        return this.getNames().get(0);
    }
}
