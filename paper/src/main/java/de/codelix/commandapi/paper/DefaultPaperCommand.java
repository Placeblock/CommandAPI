package de.codelix.commandapi.paper;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class DefaultPaperCommand extends PaperCommand<Player> {
    public DefaultPaperCommand(Plugin plugin, String label, boolean asnyc) {
        super(plugin, label, asnyc);
    }

    @Override
    protected Player getPlayer(Player player) {
        return player;
    }

    @Override
    public boolean hasPermissionPlayer(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessagePlayer(Player player, TextComponent message) {
        player.sendMessage(message);
    }
}
