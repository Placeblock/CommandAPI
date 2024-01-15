package de.codelix.commandapi.paper.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperArgument;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class DefaultPaperArgument<T, S extends PaperSource<P>, P> extends DefaultPaperNode<S, P> implements PaperArgument<T, S, P> {
    private final String name;
    private final Parameter<T, S> parameter;

    public DefaultPaperArgument(String name, Parameter<T, S> parameter, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
