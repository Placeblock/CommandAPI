package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.core.CoreLiteral;
import de.codelix.commandapi.minecraft.MinecraftSource;

import java.util.ArrayList;
import java.util.List;

public class MinecraftLiteralBuilder<S extends MinecraftSource<P, ?>, P> extends MinecraftNodeBuilder<MinecraftLiteralBuilder<S, P>, CoreLiteral<S>, S, P> implements LiteralBuilder<MinecraftLiteralBuilder<S, P>, CoreLiteral<S>, S> {
    private final List<String> names;

    public MinecraftLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public MinecraftLiteralBuilder<S, P> alias(String alias) {
        this.names.add(alias);
        return this;
    }
    @Override
    public CoreLiteral<S> build() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return new CoreLiteral<>(this.names, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }

    @Override
    protected MinecraftLiteralBuilder<S, P> getThis() {
        return this;
    }
}
