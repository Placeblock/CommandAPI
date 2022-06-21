package de.placeblock.commandapi.context;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;
import lombok.Setter;

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

    public CommandContextBuilder(S source, int start) {
        this.source = source;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder<S> copyFor(S newSource) {
        CommandContextBuilder<S> copy = new CommandContextBuilder<>(newSource, this.range.getStart());
        copy.command = this.command;
        copy.arguments.putAll(this.arguments);
        copy.nodes.addAll(this.nodes);
        copy.child = this.child;
        copy.range = this.range;
        return copy;
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
