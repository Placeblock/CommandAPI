package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.AttributeImpl;

import java.util.List;

public class AttributeBuilder<T> extends NodeBuilder<AttributeBuilder<T>, AttributeImpl<T>> {
    private final String name;
    private final Parameter<T> parameter;

    public AttributeBuilder(String name, Parameter<T> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public AttributeImpl<T> build() {
        List<Node> children = this.buildChildren();
        return new AttributeImpl<>(this.name, this.parameter, this.displayName, children, this.permission, this.optional, this.runConsumers);
    }

    @Override
    protected AttributeBuilder<T> getThis() {
        return this;
    }
}
