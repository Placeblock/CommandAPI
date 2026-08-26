package de.codelix.commandapi.velocity.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.builder.VelocityArgumentBuilder;
import de.codelix.commandapi.velocity.tree.impl.VelocityArgumentImpl;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class DefaultVelocityArgumentBuilder<T, S extends VelocitySource<P>, P> extends DefaultVelocityNodeBuilder<DefaultVelocityArgumentBuilder<T, S, P>, VelocityArgumentImpl<T, S, P>, S, P> implements VelocityArgumentBuilder<T, DefaultVelocityArgumentBuilder<T, S, P>, VelocityArgumentImpl<T, S, P>, S, P> {
    private final String name;
    private final Parameter<T, S, Component> parameter;
    private T defaultValue;

    public DefaultVelocityArgumentBuilder(String name, Parameter<T, S, Component> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public DefaultVelocityArgumentBuilder<T, S, P> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return getThis();
    }

    @Override
    public VelocityArgumentImpl<T, S, P> build() {
        List<Node<S, Component>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, Component> child : this.children) {
            children.add(child.build());
        }
        return new VelocityArgumentImpl<>(this.name, this.parameter, this.defaultValue, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }
    @Override
    protected DefaultVelocityArgumentBuilder<T, S, P> getThis() {
        return this;
    }
}
