package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.arguments.ArgumentType;
import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.context.ParsedArgument;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.suggestion.SuggestionProvider;
import de.placeblock.commandapi.suggestion.Suggestions;
import de.placeblock.commandapi.suggestion.SuggestionsBuilder;
import de.placeblock.commandapi.util.StringReader;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Getter
public class ArgumentCommandNode<S, T> extends CommandNode<S> {
    private final String name;
    private final ArgumentType<T> type;
    private final SuggestionProvider<S> customSuggestions;

    public ArgumentCommandNode(Command<S> command, String name, ArgumentType<T> type, Predicate<S> requirement, final SuggestionProvider<S> customSuggestions) {
        super(command, requirement);
        this.name = name;
        this.type = type;
        this.customSuggestions = customSuggestions;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        int start = reader.getCursor();
        T result = this.type.parse(reader);
        ParsedArgument<S, T> parsed = new ParsedArgument<>(start, reader.getCursor(), result);
        contextBuilder.withArgument(this.name, parsed);
        contextBuilder.withNode(this, parsed.getRange());
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) throws CommandSyntaxException {
        if (customSuggestions == null) {
            return type.listSuggestions(context, builder);
        } else {
            return customSuggestions.getSuggestions(context, builder);
        }
    }
}
