package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.minecraft.MinecraftSource;

public class VelocitySource<P> extends MinecraftSource<P, ConsoleCommandSource> {
    public VelocitySource(P player, ConsoleCommandSource console) {
        super(player, console);
    }
}
