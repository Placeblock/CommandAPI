package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.tree.def.DefaultArgument;
import de.codelix.commandapi.minecraft.MinecraftSource;

public interface MinecraftArgument<T, S extends MinecraftSource<P, C>, P, C> extends MinecraftNode<S, P, C>, DefaultArgument<T, S> {
}
