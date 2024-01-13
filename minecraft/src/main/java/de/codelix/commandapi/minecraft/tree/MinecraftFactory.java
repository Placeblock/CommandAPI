package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;

public interface MinecraftFactory<L extends LiteralBuilder<?, ?, S>, A extends ArgumentBuilder<?, ?, ?, S>, S> extends Factory<L, A, S> {
}
