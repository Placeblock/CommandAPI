package de.codelix.commandapi.paper.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class PaperNodeImpl<S extends PaperSource<P>, P> implements PaperNode<S, P> {
    protected final String displayName;
    protected final String description;
    protected final List<Node<S, TextComponent>> children;
    protected final String permission;
    protected final boolean unsafePermission;
    protected final boolean optional;
    protected final Collection<RunConsumer> runConsumers;
}
