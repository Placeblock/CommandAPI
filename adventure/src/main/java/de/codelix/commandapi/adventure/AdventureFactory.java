package de.codelix.commandapi.adventure;

import de.codelix.commandapi.adventure.tree.builder.AdventureArgumentBuilder;
import de.codelix.commandapi.adventure.tree.builder.AdventureLiteralBuilder;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.minecraft.MinecraftFactory;
import net.kyori.adventure.text.Component;

public interface AdventureFactory<S extends AdventureSource<P, C>, P, C> extends MinecraftFactory<S, P, C, Component> {
    AdventureLiteralBuilder<?, ?, S, P, C> literal(String name, String... aliases);

    <T> AdventureArgumentBuilder<T, ?, ?, S, P, C> argument(String name, Parameter<T, S, Component> parameter);
}
