package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.CommandAPI;
import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.tree.LiteralCommandNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LiteralArgumentBuilder<S> extends ArgumentBuilder<S, LiteralArgumentBuilder<S>> {
    private final List<String> aliases = new ArrayList<>();

    public LiteralArgumentBuilder(String name) {
        super(name);
    }

    public static <S> LiteralArgumentBuilder<S> literal(String name) {
        return new LiteralArgumentBuilder<>(name);
    }

    @Override
    public LiteralArgumentBuilder<S> getThis() {
        return this;
    }

    public LiteralArgumentBuilder<S> withAlias(String alias) {
        this.aliases.add(alias);
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Registered new Alias: " + alias);
            System.out.println("Current Aliases are: " + aliases);
        }
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
