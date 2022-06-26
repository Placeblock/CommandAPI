package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.tree.LiteralCommandNode;
import lombok.Getter;

import java.util.List;

@Getter
public class LiteralArgumentBuilder<S> extends ArgumentBuilder<S, LiteralArgumentBuilder<S>> {
    private List<String> aliases;

    public LiteralArgumentBuilder(String name) {
        super(name);
    }

    @Override
    protected LiteralArgumentBuilder<S> getThis() {
        return this;
    }

    public LiteralArgumentBuilder<S> withAlias(String alias) {
        this.aliases.add(alias);
        return getThis();
    }

    @Override
    public LiteralCommandNode<S> build() {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(getName(), getDescription(), getAliases(), getPermissions(), getCommand(), getRequirement());

        for (final CommandNode<S> child : this.getChildren()) {
            result.addChild(child);
        }

        return result;
    }
}
