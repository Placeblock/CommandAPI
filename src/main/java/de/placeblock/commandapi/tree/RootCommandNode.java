package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.util.StringReader;

public class RootCommandNode<S> extends CommandNode<S> {
    public RootCommandNode() {
        super(null);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) {
    }
}
