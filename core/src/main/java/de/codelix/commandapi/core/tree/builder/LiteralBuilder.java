package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.tree.Literal;

public interface LiteralBuilder<B extends LiteralBuilder<B, R, S>, R extends Literal<S>, S> extends NodeBuilder<B, R, S> {
    B alias(String alias);
}
