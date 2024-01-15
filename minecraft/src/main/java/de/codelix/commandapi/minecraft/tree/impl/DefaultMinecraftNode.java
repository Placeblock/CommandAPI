package de.codelix.commandapi.minecraft.tree.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class DefaultMinecraftNode<S extends MinecraftSource<P, C>, P, C> implements MinecraftNode<S, P, C> {
    protected final String displayName;
    protected final String description;
    protected final List<Node<S>> children;
    protected final String permission;
    protected final boolean unsafePermission;
    protected final boolean optional;
    protected final Collection<RunConsumer<S>> runConsumers;
}
