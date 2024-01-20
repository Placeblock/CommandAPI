package de.codelix.commandapi.adventure;

import de.codelix.commandapi.minecraft.MinecraftSource;
import net.kyori.adventure.text.TextComponent;

public abstract class AdventureSource<P, C> extends MinecraftSource<P, C, TextComponent> {
    public AdventureSource(P player, C console) {
        super(player, console);
    }

}
