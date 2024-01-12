package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.impl.LiteralImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CoreLiteral<S> implements LiteralImpl<S> {
    private final List<String> names;
    private final String displayName;
    private final List<Node<S>> children;
    private final Permission permission;
    private final boolean optional;
    private final Collection<RunConsumer<S>> runConsumers;
}
