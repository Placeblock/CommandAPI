package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class CoreLiteral<S> extends CoreNode<S> implements LiteralImpl<S> {
    private final Collection<String> names;

    public CoreLiteral(Collection<String> names, String displayName, List<Node<S>> children, Permission permission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, children, permission, optional, runConsumers);
        this.names = names;
    }
}
