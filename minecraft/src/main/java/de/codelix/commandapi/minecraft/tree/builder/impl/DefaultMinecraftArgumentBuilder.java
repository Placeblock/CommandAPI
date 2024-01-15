package de.codelix.commandapi.minecraft.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftArgumentBuilder;
import de.codelix.commandapi.minecraft.tree.impl.DefaultMinecraftArgument;

import java.util.ArrayList;
import java.util.List;

public class DefaultMinecraftArgumentBuilder<T, S extends MinecraftSource<P, C>, P, C> extends DefaultMinecraftNodeBuilder<DefaultMinecraftArgumentBuilder<T, S, P, C>, DefaultMinecraftArgument<T, S, P, C>, S, P, C> implements MinecraftArgumentBuilder<T, DefaultMinecraftArgumentBuilder<T, S, P, C>, DefaultMinecraftArgument<T, S, P, C>, S, P, C> {
    private final String name;
    private final Parameter<T, S> parameter;

    public DefaultMinecraftArgumentBuilder(String name, Parameter<T, S> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public DefaultMinecraftArgument<T, S, P, C> build() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return new DefaultMinecraftArgument<>(this.name, this.parameter, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }
    @Override
    protected DefaultMinecraftArgumentBuilder<T, S, P, C> getThis() {
        return this;
    }
}
