package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftArgumentBuilder;
import de.codelix.commandapi.minecraft.tree.builder.MinecraftLiteralBuilder;

public interface MinecraftFactory<S extends MinecraftSource<P, C, M>, P, C, M> extends Factory<S, M> {
    MinecraftLiteralBuilder<?, ?, S, P, C, M> literal(String name, String... aliases);

    <T> MinecraftArgumentBuilder<T, ?, ?, S, P, C, M> argument(String name, Parameter<T, S, M> parameter);
}
