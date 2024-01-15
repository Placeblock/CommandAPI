package de.codelix.commandapi.velocity.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityArgument;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class DefaultVelocityArgument<T, S extends VelocitySource<P>, P> extends DefaultVelocityNode<S, P> implements VelocityArgument<T, S, P> {
    private final String name;
    private final Parameter<T, S> parameter;

    public DefaultVelocityArgument(String name, Parameter<T, S> parameter, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
