package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftArgumentBuilder;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftLiteralBuilder;

public interface MinecraftFactory<L extends MinecraftLiteralBuilder<?, ?, S, P, C, M>, A extends MinecraftArgumentBuilder<?, ?, ?, S, P, C, M>, S extends MinecraftSource<P, C, M>, P, C, M> extends Factory<L, A, S, M> {
}
