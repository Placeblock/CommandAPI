package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.core.CoreArgument;
import de.codelix.commandapi.core.tree.impl.ArgumentImpl;

import java.util.List;

public class ArgumentBuilder<T> extends NodeBuilder<ArgumentBuilder<T>, ArgumentImpl<T>> {
    private final String name;
    private final Parameter<T> parameter;

    public ArgumentBuilder(String name, Parameter<T> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public ArgumentImpl<T> build() {
        List<Node> children = this.buildChildren();
        return new CoreArgument<>(this.name, this.parameter, this.displayName, children, this.permission, this.optional, this.runConsumers);
    }

    @Override
    protected ArgumentBuilder<T> getThis() {
        return this;
    }
}
