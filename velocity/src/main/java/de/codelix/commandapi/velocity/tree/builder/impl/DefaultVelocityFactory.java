package de.codelix.commandapi.velocity.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.builder.VelocityFactory;

public class DefaultVelocityFactory<S extends VelocitySource<P>, P> implements VelocityFactory<DefaultVelocityLiteralBuilder<S, P>, DefaultVelocityArgumentBuilder<?, S, P>, S, P> {
    @Override
    public DefaultVelocityLiteralBuilder<S, P> literal(String name, String... aliases) {
        return new DefaultVelocityLiteralBuilder<>(name, aliases);
    }

    @Override
    public <T> DefaultVelocityArgumentBuilder<T, S, P> argument(String name, Parameter<T, S> parameter) {
        return new DefaultVelocityArgumentBuilder<>(name, parameter);
    }
}
