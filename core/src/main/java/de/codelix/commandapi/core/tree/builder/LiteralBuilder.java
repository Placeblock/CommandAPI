package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;

import java.util.ArrayList;
import java.util.List;

public class LiteralBuilder extends NodeBuilder<LiteralBuilder, LiteralImpl> {
    protected final List<String> names;

    public LiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    @Override
    public LiteralImpl build() {
        List<Node> children = this.buildChildren();
        return new LiteralImpl(this.names, this.displayName, children, this.permission, this.optional, this.runConsumers);
    }

    @Override
    protected LiteralBuilder getThis() {
        return this;
    }
}
