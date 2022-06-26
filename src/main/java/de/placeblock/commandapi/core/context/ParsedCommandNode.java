package de.placeblock.commandapi.core.context;

import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.util.StringRange;
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
