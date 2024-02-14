package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.tree.def.DefaultNode;
import de.codelix.commandapi.minecraft.MinecraftSource;

public interface MinecraftNode<S extends MinecraftSource<P, C, M>, P, C, M> extends DefaultNode<S, M> {
}
