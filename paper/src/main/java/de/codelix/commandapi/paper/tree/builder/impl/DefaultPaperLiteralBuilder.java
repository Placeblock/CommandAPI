package de.codelix.commandapi.paper.tree.builder.impl;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperLiteralBuilder;
import de.codelix.commandapi.paper.tree.impl.PaperLiteralImpl;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class DefaultPaperLiteralBuilder<S extends PaperSource<P>, P> extends DefaultPaperNodeBuilder<DefaultPaperLiteralBuilder<S, P>, PaperLiteralImpl<S, P>, S, P> implements PaperLiteralBuilder<DefaultPaperLiteralBuilder<S, P>, PaperLiteralImpl<S, P>, S, P> {

    private final List<String> names;

    public DefaultPaperLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public DefaultPaperLiteralBuilder<S, P> alias(String alias) {
        this.names.add(alias);
        return this;
    }
    @Override
    public PaperLiteralImpl<S, P> build() {
        List<Node<S, TextComponent>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, TextComponent> child : this.children) {
            children.add(child.build());
        }
        return new PaperLiteralImpl<>(this.names, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }

    @Override
    protected DefaultPaperLiteralBuilder<S, P> getThis() {
        return this;
    }
}
