package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;

@SuppressWarnings("unused")
public class DefaultVelocitySource extends VelocitySource<Player> {
    public DefaultVelocitySource(Player player, ConsoleCommandSource console) {
        super(player, console);
    }
}
