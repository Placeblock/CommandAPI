package de.codelix.commandapi.adventure.tree;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.minecraft.tree.MinecraftNode;
import net.kyori.adventure.text.TextComponent;

public interface AdventureNode<S extends AdventureSource<P, C>, P, C> extends MinecraftNode<S, P, C, TextComponent> {
}
