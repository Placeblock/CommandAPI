package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.core.CoreArgument;
import de.codelix.commandapi.minecraft.MinecraftSource;

public class MinecraftArgumentBuilder<T, S extends MinecraftSource<P, C>, P, C> extends ArgumentBuilder<T, MinecraftArgumentBuilder<T, S, P, C>, CoreArgument<T, S>, S> {
    public MinecraftArgumentBuilder(String name, Parameter<T, S> parameter) {
        super(name, parameter);
    }

    @Override
    public CoreArgument<T, S> build() {
        return null;
    }

    @Override
    protected MinecraftArgumentBuilder<T, S, P, C> getThis() {
        return this;
    }
}
