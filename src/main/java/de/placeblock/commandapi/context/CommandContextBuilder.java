package de.placeblock.commandapi.context;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@SuppressWarnings("UnusedReturnValue")
public class CommandContextBuilder<S> {
    private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();
    private CommandNode<S> node;
    private Command<S> command;
    private StringRange range;
    private final S source;

    public CommandContextBuilder(S source, int start) {
        this.source = source;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder<S> copyFor(S newSource) {
        CommandContextBuilder<S> copy = new CommandContextBuilder<>(newSource, this.range.getStart());
        copy.command = this.command;
        copy.node = this.node;
        copy.arguments.putAll(this.arguments);
        copy.range = this.range;
        return copy;
    }

    public CommandContextBuilder<S> withArgument(String name, ParsedArgument<S, ?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public CommandContextBuilder<S> withCommand(Command<S> command) {
        this.command = command;
        return this;
    }

    public CommandContextBuilder<S> withNode(CommandNode<S> node, StringRange range) {
        this.node = node;
        this.range = StringRange.encompassing(this.range, range);
        return this;
    }

    public CommandContext<S> build(String input) {
        return new CommandContext<>(this.source, input, this.arguments, this.command, this.range);
    }

    public void print(int index) {
        System.out.println(" ".repeat(index * 5) + (this.node != null ? this.node.getName() : "null"));
        System.out.println(" ".repeat(index * 5) + this.command);
    }
}
