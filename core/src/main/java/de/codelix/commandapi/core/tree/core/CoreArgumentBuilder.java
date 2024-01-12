package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;

public class CoreArgumentBuilder<T, S> extends ArgumentBuilder<T, CoreArgumentBuilder<T, S>, CoreArgument<T, S>, S> {
    public CoreArgumentBuilder(String name, Parameter<T, S> parameter) {
        super(name, parameter);
    }

    @Override
    public CoreArgument<T, S> build() {
        return new CoreArgument<>(this.name, this.parameter, this.displayName, this.buildChildren(), this.permission, this.optional, this.runConsumers);
    }

    @Override
    protected CoreArgumentBuilder<T, S> getThis() {
        return this;
    }
}
