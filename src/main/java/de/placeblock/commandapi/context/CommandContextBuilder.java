package de.placeblock.commandapi.context;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.suggestion.SuggestionContext;
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
    private final CommandNode<S> rootNode;
    private CommandContextBuilder<S> child;
    private StringRange range;
    private final S source;

    public CommandContextBuilder(S source, int start, CommandNode<S> rootNode) {
        this.source = source;
        this.range = StringRange.at(start);
        this.rootNode = rootNode;
    }

    public CommandContextBuilder<S> copyFor(S newSource) {
        CommandContextBuilder<S> copy = new CommandContextBuilder<>(newSource, this.range.getStart(), this.rootNode);
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

    public SuggestionContext<S> findSuggestionContext(int cursor) {
        if (range.getStart() <= cursor) {
            if (range.getEnd() < cursor) {
                if (child != null) {
                    return child.findSuggestionContext(cursor);
                } else if (!nodes.isEmpty()) {
                    ParsedCommandNode<S> last = nodes.get(nodes.size() - 1);
                    return new SuggestionContext<>(last.getNode(), last.getRange().getEnd() + 1);
                } else {
                    return new SuggestionContext<>(rootNode, range.getStart());
                }
            } else {
                CommandNode<S> prev = rootNode;
                for (ParsedCommandNode<S> node : nodes) {
                    StringRange nodeRange = node.getRange();
                    if (nodeRange.getStart() <= cursor && cursor <= nodeRange.getEnd()) {
                        return new SuggestionContext<>(prev, nodeRange.getStart());
                    }
                    prev = node.getNode();
                }
                if (prev == null) {
                    throw new IllegalStateException("Can't find node before cursor");
                }
                return new SuggestionContext<>(prev, range.getStart());
            }
        }
        throw new IllegalStateException("Can't find node before cursor");
    }
}
