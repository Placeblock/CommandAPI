package de.codelix.commandapi.minecraft.tree.builder;

import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftArgument;

public interface MinecraftArgumentBuilder<T, B extends MinecraftArgumentBuilder<T, B, R, S, P, C>, R extends MinecraftArgument<T, S, P, C>, S extends MinecraftSource<P, C>, P, C> extends MinecraftNodeBuilder<B, R, S, P, C>, ArgumentBuilder<T, B, R, S> {
}
