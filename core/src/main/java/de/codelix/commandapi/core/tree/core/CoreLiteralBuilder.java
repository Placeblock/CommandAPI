package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoreLiteralBuilder<S> extends CoreNodeBuilder<CoreLiteralBuilder<S>, CoreLiteral<S>, S> implements LiteralBuilder<CoreLiteralBuilder<S>, CoreLiteral<S>, S> {
    private final Collection<String> names;

    public CoreLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public CoreLiteralBuilder<S> alias(String alias) {
        this.names.add(alias);
        return this;
    }

    @Override
    public CoreLiteral<S> build() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return new CoreLiteral<>(this.names, this.displayName, children, this.permission, this.optional, this.runConsumers);
    }

    @Override
    protected CoreLiteralBuilder<S> getThis() {
        return this;
    }
}
