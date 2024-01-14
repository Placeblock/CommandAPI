package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class CoreLiteral<S> extends CoreNode<S> implements LiteralImpl<S> {
    private final List<String> names;

    public CoreLiteral(List<String> names, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
    }
}
