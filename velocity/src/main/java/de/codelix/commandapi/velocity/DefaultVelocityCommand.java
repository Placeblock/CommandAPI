package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.adventure.AdventureDesign;
import net.kyori.adventure.text.TextComponent;

public abstract class DefaultVelocityCommand extends VelocityCommand<Player> {
    public DefaultVelocityCommand(ProxyServer proxy, String label, boolean async, AdventureDesign<VelocitySource<Player>> design) {
        super(proxy, label, async, design);
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
