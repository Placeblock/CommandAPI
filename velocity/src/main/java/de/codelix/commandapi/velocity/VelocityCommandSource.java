package de.codelix.commandapi.velocity;

import com.velocitypowered.api.command.ConsoleCommandSource;
import de.codelix.commandapi.minecraft.MCCommandSource;

public class VelocityCommandSource<P> extends MCCommandSource<P, ConsoleCommandSource>{
    public VelocityCommandSource(P player, ConsoleCommandSource console) {
        super(player, console);
    }
}
