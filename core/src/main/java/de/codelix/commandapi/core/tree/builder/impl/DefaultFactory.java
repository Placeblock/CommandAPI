package de.codelix.commandapi.core.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.builder.Factory;

public class DefaultFactory<S extends Source<M>, M> implements Factory<DefaultLiteralBuilder<S, M>, DefaultArgumentBuilder<?, S, M>, S, M> {
    @Override
    public DefaultLiteralBuilder<S, M> literal(String name, String... aliases) {
        return new DefaultLiteralBuilder<>(name, aliases);
    }

    @Override
    public <T> DefaultArgumentBuilder<T, S, M> argument(String name, Parameter<T, S, M> parameter) {
        return new DefaultArgumentBuilder<>(name, parameter);
    }
}
