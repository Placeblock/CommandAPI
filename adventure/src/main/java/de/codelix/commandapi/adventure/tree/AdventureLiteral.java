package de.codelix.commandapi.adventure.tree;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteral;

public interface AdventureLiteral<S extends AdventureSource<P, C>, P, C> extends AdventureNode<S, P, C>, MinecraftLiteral<S, P, C> {
}
