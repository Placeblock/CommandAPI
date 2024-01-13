package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.core.CoreArgument;
import de.codelix.commandapi.minecraft.MinecraftSource;

import java.util.ArrayList;
import java.util.List;

public class MinecraftArgumentBuilder<T, S extends MinecraftSource<P, ?>, P> extends MinecraftNodeBuilder<MinecraftArgumentBuilder<T, S, P>, CoreArgument<T, S>, S, P> implements ArgumentBuilder<T, MinecraftArgumentBuilder<T, S, P>, CoreArgument<T, S>, S> {
    private final String name;
    private final Parameter<T, S> parameter;

    public MinecraftArgumentBuilder(String name, Parameter<T, S> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public CoreArgument<T, S> build() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return new CoreArgument<>(this.name, this.parameter, this.displayName, children, this.permission, this.optional, this.runConsumers);
    }
    @Override
    protected MinecraftArgumentBuilder<T, S, P> getThis() {
        return this;
    }
}
