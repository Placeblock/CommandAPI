package de.codelix.commandapi.adventure.tree;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.minecraft.tree.MinecraftArgument;
import net.kyori.adventure.text.TextComponent;

public interface AdventureArgument<T, S extends AdventureSource<P, C>, P, C> extends AdventureNode<S, P, C>, MinecraftArgument<T, S, P, C, TextComponent> {
}
