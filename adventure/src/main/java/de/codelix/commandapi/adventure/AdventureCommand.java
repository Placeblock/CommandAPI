package de.codelix.commandapi.adventure;

import de.codelix.commandapi.adventure.tree.builder.AdventureArgumentBuilder;
import de.codelix.commandapi.adventure.tree.builder.AdventureLiteralBuilder;
import de.codelix.commandapi.minecraft.MinecraftCommand;
import net.kyori.adventure.text.TextComponent;

public interface AdventureCommand<S extends AdventureSource<P, C>, P, C, D extends AdventureDesign<S>, L extends AdventureLiteralBuilder<?, ?, S, P, C>, A extends AdventureArgumentBuilder<?, ?, ?, S, P, C>> extends MinecraftCommand<S, P, C, TextComponent, D, L, A> {
}
