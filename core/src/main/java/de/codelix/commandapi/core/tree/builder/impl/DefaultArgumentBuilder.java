package de.codelix.commandapi.core.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.impl.ArgumentImpl;

import java.util.ArrayList;
import java.util.List;

public class DefaultArgumentBuilder<T, S extends Source<M>, M> extends DefaultNodeBuilder<DefaultArgumentBuilder<T, S, M>, ArgumentImpl<T, S, M>, S, M> implements ArgumentBuilder<T, DefaultArgumentBuilder<T, S, M>, ArgumentImpl<T, S, M>, S, M> {
    private final String name;
    private final Parameter<T, S, M> parameter;

    public DefaultArgumentBuilder(String name, Parameter<T, S, M> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public ArgumentImpl<T, S, M> build() {
        List<Node<S, M>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, M> child : this.children) {
            children.add(child.build());
        }
        return new ArgumentImpl<>(this.name, this.parameter, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }
    @Override
    protected DefaultArgumentBuilder<T, S, M> getThis() {
        return this;
    }
}
