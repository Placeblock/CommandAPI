package de.codelix.commandapi.minecraft.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftArgument;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class DefaultMinecraftArgument<T, S extends MinecraftSource<P, C>, P, C> extends DefaultMinecraftNode<S, P, C> implements MinecraftArgument<T, S, P, C> {
    private final String name;
    private final Parameter<T, S> parameter;

    public DefaultMinecraftArgument(String name, Parameter<T, S> parameter, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }
}
