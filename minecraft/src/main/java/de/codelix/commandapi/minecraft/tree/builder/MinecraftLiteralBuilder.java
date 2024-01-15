package de.codelix.commandapi.minecraft.tree.builder;

import de.codelix.commandapi.core.tree.builder.LiteralBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteral;

public interface MinecraftLiteralBuilder<B extends MinecraftLiteralBuilder<B, R, S, P, C>, R extends MinecraftLiteral<S, P, C>, S extends MinecraftSource<P, C>, P, C> extends MinecraftNodeBuilder<B, R, S, P, C>, LiteralBuilder<B, R, S> {
}
