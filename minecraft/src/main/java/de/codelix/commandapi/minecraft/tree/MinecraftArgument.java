package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.tree.def.DefaultArgument;
import de.codelix.commandapi.minecraft.MinecraftSource;

public interface MinecraftArgument<T, S extends MinecraftSource<P, C, M>, P, C, M> extends MinecraftNode<S, P, C, M>, DefaultArgument<T, S, M> {
}
