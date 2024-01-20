package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Argument;

public interface ArgumentBuilder<T, B extends ArgumentBuilder<T, B, R, S, M>, R extends Argument<T, S, M>, S extends Source<M>, M> extends NodeBuilder<B, R, S, M> {

}
