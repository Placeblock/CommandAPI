package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.suggestion.Suggestions;
import de.placeblock.commandapi.suggestion.SuggestionsBuilder;
import de.placeblock.commandapi.util.StringReader;

import java.util.concurrent.CompletableFuture;

public class RootCommandNode<S> extends CommandNode<S> {
    public RootCommandNode() {
        super(null, null);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        return null;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) {
    }
}
