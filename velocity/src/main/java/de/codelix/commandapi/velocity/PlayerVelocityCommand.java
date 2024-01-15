package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.adventure.AdventureDesign;
import net.kyori.adventure.text.TextComponent;

@SuppressWarnings("unused")
public abstract class PlayerVelocityCommand extends DefaultVelocityCommand<Player> {

    public PlayerVelocityCommand(ProxyServer proxy, String label, AdventureDesign<VelocitySource<Player>> design) {
        super(proxy, label, design);
    }

    public PlayerVelocityCommand(ProxyServer proxy, String label) {
        super(proxy, label);
    }

    @Override
    protected Player getPlayer(Player player) {
        return player;
    }

    @Override
    void sendMessagePlayer(Player source, TextComponent message) {
        source.sendMessage(message);
    }

    @Override
    boolean hasPermissionPlayer(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
