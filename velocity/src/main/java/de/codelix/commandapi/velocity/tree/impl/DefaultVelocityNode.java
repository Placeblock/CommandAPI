package de.codelix.commandapi.velocity.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class DefaultVelocityNode<S extends VelocitySource<P>, P> implements VelocityNode<S, P> {
    protected final String displayName;
    protected final String description;
    protected final List<Node<S>> children;
    protected final String permission;
    protected final boolean unsafePermission;
    protected final boolean optional;
    protected final Collection<RunConsumer<S>> runConsumers;
}
