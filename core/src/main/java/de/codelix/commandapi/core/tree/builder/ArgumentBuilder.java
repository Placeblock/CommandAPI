package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.tree.Argument;

public interface ArgumentBuilder<T, B extends ArgumentBuilder<T, B, R, S>, R extends Argument<T, S>, S> extends NodeBuilder<B, R, S> {

}
