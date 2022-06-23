package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.suggestion.Suggestions;
import de.placeblock.commandapi.suggestion.SuggestionsBuilder;
import de.placeblock.commandapi.util.StringRange;
import de.placeblock.commandapi.util.StringReader;

import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class LiteralCommandNode<S> extends CommandNode<S> {
    private final String label;

    public LiteralCommandNode(Command<S> command, String label, Predicate<S> requirement) {
        super(command, requirement);
        this.label = label;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        int start = reader.getCursor();
        int end = parse(reader);
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }
        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, this.label);
    }

    private int parse(StringReader reader) {
        int start = reader.getCursor();
        if (reader.canRead(this.label.length())) {
            int end = start + this.label.length();
            if (reader.getString().substring(start, end).equals(this.label)) {
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                } else {
                    reader.setCursor(start);
                }
            }
        }
        return -1;
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        if (this.label.toLowerCase().startsWith(builder.getRemainingLowerCase())) {
            return builder.suggest(this.label).buildFuture();
        } else {
            return Suggestions.empty();
        }
    }

    @Override
    public String getName() {
        return this.label;
    }

}
