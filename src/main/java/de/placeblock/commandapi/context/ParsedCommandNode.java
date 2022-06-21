package de.placeblock.commandapi.context;

import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.util.StringRange;
import lombok.Getter;

@Getter
public class ParsedCommandNode<S> {
    private final CommandNode<S> node;

    private final StringRange range;

    public ParsedCommandNode(CommandNode<S> node, StringRange range) {
        this.node = node;
        this.range = range;
    }
}
