package de.codelix.commandapi.adventure;

import de.codelix.commandapi.minecraft.MinecraftCommand;
import net.kyori.adventure.text.TextComponent;

public interface AdventureCommand<S extends AdventureSource<P, C>, P, C, D extends AdventureDesign<S>> extends MinecraftCommand<S, P, C, TextComponent, D> {
}
