package de.placeblock.commandapi.core.context;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.util.StringRange;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@SuppressWarnings("UnusedReturnValue")
public class CommandContextBuilder<S> {
    private final Map<String, ParsedArgument<S, ?>> arguments = new LinkedHashMap<>();
    private final List<CommandSyntaxException> exceptions = new ArrayList<>();
    private CommandNode<S> node;
    private CommandContextBuilder<S> child;
    private Command<S> command;
    private StringRange range;
    private final S source;

    public CommandContextBuilder(S source, int start) {
        this.source = source;
        this.range = StringRange.at(start);
    }

    public CommandContextBuilder<S> withArgument(String name, ParsedArgument<S, ?> argument) {
        this.arguments.put(name, argument);
        return this;
    }

    public CommandContextBuilder<S> withException(CommandSyntaxException ex) {
        this.exceptions.add(ex);
        return this;
    }

    public CommandContextBuilder<S> withChild(CommandContextBuilder<S> child) {
        this.child = child;
        return this;
    }

    public CommandContextBuilder<S> getLastChild() {
        CommandContextBuilder<S> currentCCB = this;
        while (currentCCB.getChild() != null) {
            currentCCB = currentCCB.getChild();
        }
        return currentCCB;
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
        return new CommandContext<>(this.source, input, this.child != null ? this.child.build(input) : null, this.arguments, this.command, this.range);
    }

    public void print(int index) {
        System.out.println(" ".repeat(index * 5) + (this.node != null ? this.node.getName() : "null"));
        if (this.child != null) {
            this.child.print(index + 1);
        }
    }
}
