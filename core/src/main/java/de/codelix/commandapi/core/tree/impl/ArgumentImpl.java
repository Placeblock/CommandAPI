package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.def.DefaultArgument;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class ArgumentImpl<T, S> extends NodeImpl<S> implements DefaultArgument<T, S> {
    private final String name;
    private final Parameter<T, S> parameter;

    public ArgumentImpl(String name, Parameter<T, S> parameter, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
