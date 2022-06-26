package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.tree.LiteralCommandNode;
import lombok.Getter;

@Getter
public class LiteralArgumentBuilder<S> extends ArgumentBuilder<S, LiteralArgumentBuilder<S>> {
    private final String literal;

    public LiteralArgumentBuilder(String literal) {
        this.literal = literal;
    }

    @Override
    protected LiteralArgumentBuilder<S> getThis() {
        return this;
    }

    @Override
    public LiteralCommandNode<S> build() {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(getCommand(), getLiteral(), getRequirement());

        for (final CommandNode<S> child : this.getChildren()) {
            result.addChild(child);
        }

        return result;
    }
}
