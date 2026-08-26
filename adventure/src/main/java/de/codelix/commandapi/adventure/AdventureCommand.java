package de.codelix.commandapi.adventure;

import de.codelix.commandapi.adventure.tree.builder.AdventureArgumentBuilder;
import de.codelix.commandapi.adventure.tree.builder.AdventureLiteralBuilder;
import de.codelix.commandapi.minecraft.MinecraftCommand;
import net.kyori.adventure.text.Component;

public interface AdventureCommand<S extends AdventureSource<P, C>, P, C, D extends AdventureDesign, L extends AdventureLiteralBuilder<?, ?, S, P, C>, A extends AdventureArgumentBuilder<?, ?, ?, S, P, C>> extends MinecraftCommand<S, P, C, Component, D, L, A> {
}
