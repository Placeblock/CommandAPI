package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.Source;

public interface Factory<S extends Source<M>, M> {

    LiteralBuilder<?, ?, S, M> literal(String name, String... aliases);

    <T> ArgumentBuilder<T, ?, ?, S, M> argument(String name, Parameter<T, S, M> parameter);

}
