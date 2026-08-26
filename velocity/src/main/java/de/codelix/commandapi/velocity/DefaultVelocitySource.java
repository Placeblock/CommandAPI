package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public class DefaultVelocitySource extends VelocitySource<Player> {
    @Override
    public void sendMessagePlayer(Component message) {
        this.getPlayer().sendMessage(message);
    }

    public DefaultVelocitySource(Player player, ConsoleCommandSource console) {
        super(player, console);
    }

    @Override
    public boolean hasPermissionPlayer(String permission) {
        return this.getPlayer().hasPermission(permission);
    }
}
