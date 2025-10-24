package de.codelix.commandapi.velocity.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityArgument;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.Collection;
import java.util.List;

@Getter
public class VelocityArgumentImpl<T, S extends VelocitySource<P>, P> extends VelocityNodeImpl<S, P> implements VelocityArgument<T, S, P> {
    private final String name;
    private final Parameter<T, S, TextComponent> parameter;
    private final T defaultValue;

    public VelocityArgumentImpl(String name, Parameter<T, S, TextComponent> parameter, T defaultValue, String displayName, String description, List<Node<S, TextComponent>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
        this.defaultValue = defaultValue;
    }
}
