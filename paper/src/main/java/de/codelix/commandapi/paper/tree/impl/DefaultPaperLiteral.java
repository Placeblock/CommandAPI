package de.codelix.commandapi.paper.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperLiteral;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class DefaultPaperLiteral<S extends PaperSource<P>, P> extends DefaultPaperNode<S, P> implements PaperLiteral<S, P> {
    private final List<String> names;

    public DefaultPaperLiteral(List<String> names, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
    }
}
