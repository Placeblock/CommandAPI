package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.minecraft.MinecraftSource;

public class MinecraftFactory<S extends MinecraftSource<P, ?>, P> implements Factory<MinecraftLiteralBuilder<S, P>, MinecraftArgumentBuilder<?, S, P>, S> {
    @Override
    public MinecraftLiteralBuilder<S, P> literal(String name, String... aliases) {
        return new MinecraftLiteralBuilder<>(name, aliases);
    }

    @Override
    public <T> MinecraftArgumentBuilder<?, S, P> argument(String name, Parameter<T, S> parameter) {
        return new MinecraftArgumentBuilder<>(name, parameter);
    }
}
