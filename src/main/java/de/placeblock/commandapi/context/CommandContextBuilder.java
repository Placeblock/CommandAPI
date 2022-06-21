package de.placeblock.commandapi.context;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings("UnusedReturnValue")
public class CommandContextBuilder<S> {
    private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();
    private final List<ParsedCommandNode<S>> nodes = new ArrayList<>();
    private Command<S> command;
    private CommandContextBuilder<S> child;
    private StringRange range;
    private final S source;

    public CommandContextBuilder(S source) {
        this.source = source;
    }

    public CommandContextBuilder<S> copy() {
        return new CommandContextBuilder<>(this.source);
    }

    public CommandContextBuilder<S> withArgument(String name, ParsedArgument<S, ?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public CommandContextBuilder<S> withChild(CommandContextBuilder<S> child) {
        this.child = child;
        return this;
    }

    public CommandContextBuilder<S> withNode(CommandNode<S> node, StringRange range) {
        nodes.add(new ParsedCommandNode<>(node, range));
        this.range = StringRange.encompassing(this.range, range);
        return this;
    }

    public CommandContextBuilder<S> withCommand(Command<S> command) {
        this.command = command;
        return this;
    }

    public CommandContext<S> build(String input) {
        return new CommandContext<>(this.source, input, this.arguments, this.command, this.nodes, this.range, this.child == null ? null : this.child.build(input));
    }
}
