package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.tree.Literal;

import java.util.ArrayList;
import java.util.List;

public abstract class LiteralBuilder<B extends LiteralBuilder<B, R, S>, R extends Literal<S>, S> extends NodeBuilder<B, R, S> {
    protected final List<String> names;

    public LiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }
}
