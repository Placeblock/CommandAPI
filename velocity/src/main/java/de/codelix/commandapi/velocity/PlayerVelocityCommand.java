package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.adventure.AdventureDesign;

@SuppressWarnings("unused")
public abstract class PlayerVelocityCommand extends DefaultVelocityCommand<DefaultVelocitySource, Player> {

    public PlayerVelocityCommand(ProxyServer proxy, String label, AdventureDesign<DefaultVelocitySource> design) {
        super(proxy, label, design);
    }

    public PlayerVelocityCommand(ProxyServer proxy, String label) {
        super(proxy, label);
    }

    @Override
    protected DefaultVelocitySource createSource(Player player, ConsoleCommandSource console) {
        return new DefaultVelocitySource(player, console);
    }

    @Override
    protected Player getPlayer(Player player) {
        return player;
    }
}
