package de.codelix.commandapi.core.tree.builder.impl;

import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;

import java.util.ArrayList;
import java.util.List;

public class DefaultLiteralBuilder<S extends Source<M>, M> extends DefaultNodeBuilder<DefaultLiteralBuilder<S, M>, LiteralImpl<S, M>, S, M> implements LiteralBuilder<DefaultLiteralBuilder<S, M>, LiteralImpl<S, M>, S, M> {

    private final List<String> names;

    public DefaultLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public DefaultLiteralBuilder<S, M> alias(String alias) {
        this.names.add(alias);
        return this;
    }
    @Override
    public LiteralImpl<S, M> build() {
        List<Node<S, M>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, M> child : this.children) {
            children.add(child.build());
        }
        return new LiteralImpl<>(this.names, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }

    @Override
    protected DefaultLiteralBuilder<S, M> getThis() {
        return this;
    }
}
