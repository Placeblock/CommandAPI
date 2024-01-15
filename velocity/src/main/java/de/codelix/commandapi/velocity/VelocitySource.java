package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.AdventureSource;

public class VelocitySource<P> extends AdventureSource<P, ConsoleCommandSource> {
    public VelocitySource(P player, ConsoleCommandSource console) {
        super(player, console);
    }
}
