package de.codelix.commandapi.minecraft.tree.builder.impl;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftLiteralBuilder;
import de.codelix.commandapi.minecraft.tree.impl.DefaultMinecraftLiteral;

import java.util.ArrayList;
import java.util.List;

public class DefaultMinecraftLiteralBuilder<S extends MinecraftSource<P, C>, P, C> extends DefaultMinecraftNodeBuilder<DefaultMinecraftLiteralBuilder<S, P, C>, DefaultMinecraftLiteral<S, P, C>, S, P, C> implements MinecraftLiteralBuilder<DefaultMinecraftLiteralBuilder<S, P, C>, DefaultMinecraftLiteral<S, P, C>, S, P, C> {
    private final List<String> names;

    public DefaultMinecraftLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public DefaultMinecraftLiteralBuilder<S, P, C> alias(String alias) {
        this.names.add(alias);
        return this;
    }
    @Override
    public DefaultMinecraftLiteral<S, P, C> build() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return new DefaultMinecraftLiteral<>(this.names, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }

    @Override
    protected DefaultMinecraftLiteralBuilder<S, P, C> getThis() {
        return this;
    }
}
