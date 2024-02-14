package de.codelix.commandapi.velocity.tree.builder.impl;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.builder.VelocityLiteralBuilder;
import de.codelix.commandapi.velocity.tree.impl.VelocityLiteralImpl;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

public class DefaultVelocityLiteralBuilder<S extends VelocitySource<P>, P> extends DefaultVelocityNodeBuilder<DefaultVelocityLiteralBuilder<S, P>, VelocityLiteralImpl<S, P>, S, P> implements VelocityLiteralBuilder<DefaultVelocityLiteralBuilder<S, P>, VelocityLiteralImpl<S, P>, S, P> {

    private final List<String> names;

    public DefaultVelocityLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public DefaultVelocityLiteralBuilder<S, P> alias(String alias) {
        this.names.add(alias);
        return this;
    }
    @Override
    public VelocityLiteralImpl<S, P> build() {
        List<Node<S, TextComponent>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, TextComponent> child : this.children) {
            children.add(child.build());
        }
        return new VelocityLiteralImpl<>(this.names, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers);
    }

    @Override
    protected DefaultVelocityLiteralBuilder<S, P> getThis() {
        return this;
    }
}
