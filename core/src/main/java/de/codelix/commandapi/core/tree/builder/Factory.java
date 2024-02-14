package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.Source;

public interface Factory<L extends LiteralBuilder<?, ?, S, M>, A extends ArgumentBuilder<?, ?, ?, S, M>, S extends Source<M>, M> {

    L literal(String name, String... aliases);

    <T> A argument(String name, Parameter<T, S, M> parameter);

}
