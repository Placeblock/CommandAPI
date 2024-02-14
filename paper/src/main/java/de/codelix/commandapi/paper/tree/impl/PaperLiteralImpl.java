package de.codelix.commandapi.paper.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperLiteral;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;

@Getter
public class PaperLiteralImpl<S extends PaperSource<P>, P> extends PaperNodeImpl<S, P> implements PaperLiteral<S, P> {
    private final List<String> names;

    public PaperLiteralImpl(List<String> names, String displayName, String description, List<Node<S, TextComponent>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
    }
}
