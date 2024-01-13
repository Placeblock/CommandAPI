package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.InvalidLiteralSyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.tree.Literal;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LiteralImpl<S> extends NodeImpl<S>, Literal<S> {
    @Override
    default void parse(ParseContext<S> ctx, ParsedCommand<S> parsedCommand) throws SyntaxException {
        String next = ctx.getInput().poll();
        if (!this.getNames().contains(next)) {
            throw new InvalidLiteralSyntaxException(this);
        }
    }

    @Override
    default CompletableFuture<List<String>> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
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
