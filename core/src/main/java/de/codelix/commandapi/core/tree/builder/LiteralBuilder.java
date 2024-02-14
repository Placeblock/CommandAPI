package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Literal;

public interface LiteralBuilder<B extends LiteralBuilder<B, R, S, M>, R extends Literal<S, M>, S extends Source<M>, M> extends NodeBuilder<B, R, S, M> {
    B alias(String alias);
}
