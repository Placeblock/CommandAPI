package de.codelix.commandapi.adventure.tree.builder;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.adventure.tree.AdventureNode;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftNodeBuilder;
import net.kyori.adventure.text.TextComponent;

public interface AdventureNodeBuilder<B extends AdventureNodeBuilder<B, R, S, P, C>, R extends AdventureNode<S, P, C>, S extends AdventureSource<P, C>, P, C> extends MinecraftNodeBuilder<B, R, S, P, C, TextComponent> {
}
