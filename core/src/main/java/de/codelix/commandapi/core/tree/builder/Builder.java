package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;

public interface Builder {

    LiteralBuilder literal(String name, String... aliases);

    <T> ArgumentBuilder<T> argument(String name, Parameter<T> parameter);

}
