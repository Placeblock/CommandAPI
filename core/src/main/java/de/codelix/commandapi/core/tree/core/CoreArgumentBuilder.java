package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class CoreArgumentBuilder<T, S> extends CoreNodeBuilder<CoreArgumentBuilder<T, S>, CoreArgument<T, S>, S> implements ArgumentBuilder<T, CoreArgumentBuilder<T, S>, CoreArgument<T, S>, S> {
    private final String name;
    private final Parameter<T, S> parameter;

    public CoreArgumentBuilder(String name, Parameter<T, S> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public CoreArgument<T, S> build() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return new CoreArgument<>(this.name, this.parameter, this.displayName, this.description, children, this.permission, this.unsafePermission, this.optional, this.runConsumers);
    }

    @Override
    protected CoreArgumentBuilder<T, S> getThis() {
        return this;
    }
}
