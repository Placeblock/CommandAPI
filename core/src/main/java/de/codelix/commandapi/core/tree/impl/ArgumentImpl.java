package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ArgumentImpl<T, S> extends NodeImpl<S>, Argument<T, S> {
    @Override
    default void parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws SyntaxException {
        T value = this.getParameter().parse(ctx, cmd);
        cmd.storeArgument(this, value);
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
