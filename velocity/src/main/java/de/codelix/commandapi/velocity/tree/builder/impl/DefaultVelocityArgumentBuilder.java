package de.codelix.commandapi.velocity.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.builder.VelocityArgumentBuilder;
import de.codelix.commandapi.velocity.tree.impl.DefaultVelocityArgument;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class DefaultVelocityArgumentBuilder<T, S extends VelocitySource<P>, P> extends DefaultVelocityNodeBuilder<DefaultVelocityArgumentBuilder<T, S, P>, DefaultVelocityArgument<T, S, P>, S, P> implements VelocityArgumentBuilder<T, DefaultVelocityArgumentBuilder<T, S, P>, DefaultVelocityArgument<T, S, P>, S, P> {
    private final String name;
    private final Parameter<T, S, TextComponent> parameter;

    public DefaultVelocityArgumentBuilder(String name, Parameter<T, S, TextComponent> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public DefaultVelocityArgument<T, S, P> build() {
        List<Node<S, TextComponent>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, TextComponent> child : this.children) {
            children.add(child.build());
        }
        return new DefaultVelocityArgument<>(this.name, this.parameter, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }
    @Override
    protected DefaultVelocityArgumentBuilder<T, S, P> getThis() {
        return this;
    }
}
