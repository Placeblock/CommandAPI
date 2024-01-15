package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.tree.def.DefaultLiteral;
import de.codelix.commandapi.minecraft.MinecraftSource;

public interface MinecraftLiteral<S extends MinecraftSource<P, C>, P, C> extends MinecraftNode<S, P, C>, DefaultLiteral<S> {
}
