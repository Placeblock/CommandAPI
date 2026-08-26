package de.codelix.commandapi.adventure;

import de.codelix.commandapi.minecraft.MinecraftSource;
import net.kyori.adventure.text.Component;

public abstract class AdventureSource<P, C> extends MinecraftSource<P, C, Component> {
    public AdventureSource(P player, C console) {
        super(player, console);
    }

}
