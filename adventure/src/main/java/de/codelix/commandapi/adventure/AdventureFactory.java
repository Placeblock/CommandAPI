package de.codelix.commandapi.adventure;

import de.codelix.commandapi.adventure.tree.builder.AdventureArgumentBuilder;
import de.codelix.commandapi.adventure.tree.builder.AdventureLiteralBuilder;
import de.codelix.commandapi.minecraft.MinecraftFactory;
import net.kyori.adventure.text.TextComponent;

public interface AdventureFactory<L extends AdventureLiteralBuilder<?, ?, S, P, C>, A extends AdventureArgumentBuilder<?, ?, ?, S, P, C>, S extends AdventureSource<P, C>, P, C> extends MinecraftFactory<L, A, S, P, C, TextComponent> {
}
