package de.placeblock.commandapi;

import de.placeblock.commandapi.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.tree.LiteralCommandNode;

public abstract class CommandAPICommand<S> {

    private final LiteralCommandNode<S> commandNode;

    public CommandAPICommand() {
        this.commandNode = this.generateCommand().build();
    }

    public abstract LiteralArgumentBuilder<S> generateCommand();;


}
