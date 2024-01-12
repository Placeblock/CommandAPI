package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;

public interface Factory<L extends LiteralBuilder<?, ?, S>, A extends ArgumentBuilder<?, ?, ?, S>, S> {

    L literal(String name, String... aliases);

    <T> A argument(String name, Parameter<T, S> parameter);

}
