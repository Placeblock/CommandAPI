package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.InvalidArgumentParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Argument;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArgumentImpl<T, S> extends NodeImpl<S>, Argument<T, S> {
    @Override
    default void parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException {
        T value = this.getParameter().parse(ctx, cmd);
        if (value != null) {
            cmd.storeArgument(this, value);
        } else {
            throw new InvalidArgumentParseException(this);
        }
    }

    @Override
    default CompletableFuture<List<String>> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        return this.getParameter().getSuggestionsAsync(ctx, cmd);
    }

    @Override
    default @NonNull String getDisplayNameSafe() {
        String displayName = this.getDisplayName();
        if (displayName != null) return displayName;
        return this.getName();
    }
}
