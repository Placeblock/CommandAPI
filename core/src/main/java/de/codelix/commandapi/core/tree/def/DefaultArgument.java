package de.codelix.commandapi.core.tree.def;

import de.codelix.commandapi.core.exception.InvalidArgumentParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Argument;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DefaultArgument<T, S extends Source<M>, M> extends DefaultNode<S, M>, Argument<T, S, M> {
    @Override
    default void parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) throws ParseException {
        T value = this.getParameter().parse(ctx, cmd);
        if (value != null) {
            cmd.storeArgument(this, value);
        } else {
            throw new InvalidArgumentParseException(this);
        }
    }

    @Override
    default CompletableFuture<List<String>> getSuggestions(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        return this.getParameter().getSuggestionsAsync(ctx, cmd);
    }

    @Override
    default @NonNull String getDisplayNameSafe() {
        String displayName = this.getDisplayName();
        if (displayName != null) return displayName;
        return this.getName();
    }
}
