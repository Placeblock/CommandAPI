package de.codelix.commandapi.paper.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperArgument;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;

@Getter
public class PaperArgumentImpl<T, S extends PaperSource<P>, P> extends PaperNodeImpl<S, P> implements PaperArgument<T, S, P> {
    private final String name;
    private final Parameter<T, S, TextComponent> parameter;

    public PaperArgumentImpl(String name, Parameter<T, S, TextComponent> parameter, String displayName, String description, List<Node<S, TextComponent>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
