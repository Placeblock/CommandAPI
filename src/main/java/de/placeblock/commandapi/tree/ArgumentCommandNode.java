package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.argument.ArgumentType;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.context.ParsedArgument;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringReader;

public class ArgumentCommandNode<S, T> extends CommandNode<S> {
    private final String name;
    private final ArgumentType<T> type;

    public ArgumentCommandNode(Command<S> command, String name, ArgumentType<T> type) {
        super(command);
        this.name = name;
        this.type = type;
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
}
