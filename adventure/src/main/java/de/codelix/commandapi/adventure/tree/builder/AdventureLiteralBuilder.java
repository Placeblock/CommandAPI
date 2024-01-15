package de.codelix.commandapi.adventure.tree.builder;

import de.codelix.commandapi.adventure.AdventureSource;
import de.codelix.commandapi.adventure.tree.AdventureLiteral;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftLiteralBuilder;

public interface AdventureLiteralBuilder<B extends AdventureLiteralBuilder<B, R, S, P, C>, R extends AdventureLiteral<S, P, C>, S extends AdventureSource<P, C>, P, C> extends AdventureNodeBuilder<B, R, S, P, C>, MinecraftLiteralBuilder<B, R, S, P, C> {
}
