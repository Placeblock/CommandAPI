package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.ArgumentImpl;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class CoreArgument<T, S> extends CoreNode<S> implements ArgumentImpl<T, S> {
    private final String name;
    private final Parameter<T, S> parameter;

    public CoreArgument(String name, Parameter<T, S> parameter, String displayName, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
