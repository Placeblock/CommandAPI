package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Argument;

public abstract class ArgumentBuilder<T, B extends ArgumentBuilder<T, B, R, S>, R extends Argument<T, S>, S> extends NodeBuilder<B, R, S> {
    protected final String name;
    protected final Parameter<T, S> parameter;

    public ArgumentBuilder(String name, Parameter<T, S> parameter) {
        this.name = name;
        this.parameter = parameter;
    }
}
