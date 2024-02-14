package de.codelix.commandapi.minecraft.tree.builder;

import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftArgument;

public interface MinecraftArgumentBuilder<T, B extends MinecraftArgumentBuilder<T, B, R, S, P, C, M>, R extends MinecraftArgument<T, S, P, C, M>, S extends MinecraftSource<P, C, M>, P, C, M> extends MinecraftNodeBuilder<B, R, S, P, C, M>, ArgumentBuilder<T, B, R, S, M> {
}
