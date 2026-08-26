package de.codelix.commandapi.paper.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.impl.PaperArgumentImpl;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class DefaultPaperArgumentBuilder<T, S extends PaperSource<P>, P> extends DefaultPaperNodeBuilder<DefaultPaperArgumentBuilder<T, S, P>, PaperArgumentImpl<T, S, P>, S, P> implements PaperArgumentBuilder<T, DefaultPaperArgumentBuilder<T, S, P>, PaperArgumentImpl<T, S, P>, S, P> {
    private final String name;
    private final Parameter<T, S, Component> parameter;
    private T defaultValue;

    public DefaultPaperArgumentBuilder(String name, Parameter<T, S, Component> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public DefaultPaperArgumentBuilder<T, S, P> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return getThis();
    }

    @Override
    public PaperArgumentImpl<T, S, P> build() {
        List<Node<S, Component>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, Component> child : this.children) {
            children.add(child.build());
        }
        return new PaperArgumentImpl<>(this.name, this.parameter, this.defaultValue, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }
    @Override
    protected DefaultPaperArgumentBuilder<T, S, P> getThis() {
        return this;
    }
}
