package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.Factory;

public class CoreFactory<S> implements Factory<CoreLiteralBuilder<S>, CoreArgumentBuilder<?, S>, S> {
    @Override
    public CoreLiteralBuilder<S> literal(String name, String... aliases) {
        return new CoreLiteralBuilder<>(name, aliases);
    }

    @Override
    public <T> CoreArgumentBuilder<T, S> argument(String name, Parameter<T, S> parameter) {
        return new CoreArgumentBuilder<>(name, parameter);
    }
}
