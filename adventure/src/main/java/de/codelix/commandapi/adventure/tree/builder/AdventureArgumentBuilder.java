package de.codelix.commandapi.adventure.tree.builder;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.adventure.tree.AdventureArgument;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftArgumentBuilder;
import net.kyori.adventure.text.TextComponent;

public interface AdventureArgumentBuilder<T, B extends AdventureArgumentBuilder<T, B, R, S, P, C>, R extends AdventureArgument<T, S, P, C>, S extends AdventureSource<P, C>, P, C> extends AdventureNodeBuilder<B, R, S, P, C>, MinecraftArgumentBuilder<T, B, R, S, P, C, TextComponent> {
}
