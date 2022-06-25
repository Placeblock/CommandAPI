package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringRange;
import de.placeblock.commandapi.util.StringReader;

import java.util.ArrayList;
import java.util.List;
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
    public CompletableFuture<List<String>> listSuggestions(CommandContext<S> context, String partial) {
        if (this.label.toLowerCase().startsWith(partial)) {
            return CompletableFuture.completedFuture(List.of(this.label));
        } else {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
    }

    @Override
    public String getName() {
        return this.label;
    }

}
