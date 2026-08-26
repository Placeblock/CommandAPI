package de.codelix.commandapi.adventure.tree;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.minecraft.tree.MinecraftNode;
import net.kyori.adventure.text.Component;

public interface AdventureNode<S extends AdventureSource<P, C>, P, C> extends MinecraftNode<S, P, C, Component> {
}
