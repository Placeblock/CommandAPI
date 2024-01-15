package de.codelix.commandapi.adventure;

import de.codelix.commandapi.minecraft.MinecraftSource;

public class AdventureSource<P, C> extends MinecraftSource<P, C> {
    public AdventureSource(P player, C console) {
        super(player, console);
    }
}
