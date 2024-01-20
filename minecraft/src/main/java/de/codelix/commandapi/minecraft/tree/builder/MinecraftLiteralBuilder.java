package de.codelix.commandapi.minecraft.tree.builder;

import de.codelix.commandapi.core.tree.builder.LiteralBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteral;

public interface MinecraftLiteralBuilder<B extends MinecraftLiteralBuilder<B, R, S, P, C, M>, R extends MinecraftLiteral<S, P, C, M>, S extends MinecraftSource<P, C, M>, P, C, M> extends MinecraftNodeBuilder<B, R, S, P, C, M>, LiteralBuilder<B, R, S, M> {
}
