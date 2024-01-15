package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.def.DefaultLiteral;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class LiteralImpl<S> extends NodeImpl<S> implements DefaultLiteral<S> {
    private final List<String> names;

    public LiteralImpl(List<String> names, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
    }
}
