package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.def.DefaultArgument;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class ArgumentImpl<T, S extends Source<M>, M> extends NodeImpl<S, M> implements DefaultArgument<T, S, M> {
    private final String name;
    private final Parameter<T, S, M> parameter;

    public ArgumentImpl(String name, Parameter<T, S, M> parameter, String displayName, String description, List<Node<S, M>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
