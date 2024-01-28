package de.codelix.commandapi.paper.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.impl.DefaultPaperArgument;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class DefaultPaperArgumentBuilder<T, S extends PaperSource<P>, P> extends DefaultPaperNodeBuilder<DefaultPaperArgumentBuilder<T, S, P>, DefaultPaperArgument<T, S, P>, S, P> implements PaperArgumentBuilder<T, DefaultPaperArgumentBuilder<T, S, P>, DefaultPaperArgument<T, S, P>, S, P> {
    private final String name;
    private final Parameter<T, S, TextComponent> parameter;

    public DefaultPaperArgumentBuilder(String name, Parameter<T, S, TextComponent> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public DefaultPaperArgument<T, S, P> build() {
        List<Node<S, TextComponent>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, TextComponent> child : this.children) {
            children.add(child.build());
        }
        return new DefaultPaperArgument<>(this.name, this.parameter, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }
    @Override
    protected DefaultPaperArgumentBuilder<T, S, P> getThis() {
        return this;
    }
}
