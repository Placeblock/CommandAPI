package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.NodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class CoreNode<S> implements NodeImpl<S> {
    protected final String displayName;
    protected final String description;
    protected final List<Node<S>> children;
    protected final String permission;
    protected final boolean unsafePermission;
    protected final boolean optional;
    protected final Collection<RunConsumer<S>> runConsumers;
}
