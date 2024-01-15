package de.codelix.commandapi.minecraft.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteral;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class DefaultMinecraftLiteral<S extends MinecraftSource<P, C>, P, C> extends DefaultMinecraftNode<S, P, C> implements MinecraftLiteral<S, P, C> {
    private final List<String> names;

    public DefaultMinecraftLiteral(List<String> names, String displayName, String description, List<Node<S>> children, String permission, boolean unsafePermission, boolean optional, Collection<RunConsumer<S>> runConsumers) {
        super(displayName, description, children, permission, unsafePermission, optional, runConsumers);
        this.names = names;
    }
}
