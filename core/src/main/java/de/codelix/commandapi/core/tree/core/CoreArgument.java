package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.ArgumentImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CoreArgument<T, S> implements ArgumentImpl<T, S> {
    private final String name;
    private final Parameter<T, S> parameter;
    private final String displayName;
    private final List<Node<S>> children;
    private final Permission permission;
    private final boolean optional;
    private final Collection<RunConsumer<S>> runConsumers;
}
