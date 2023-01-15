package de.placeblock.commandapi.bridge.paper;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class PaperCommandBridge<PL extends JavaPlugin> extends AbstractPaperCommandBridge<PL, Player> {
    public PaperCommandBridge(PL plugin, String label) {
        super(plugin, label);
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(Player player, TextComponent message) {
        player.sendMessage(message);
    }

    @Override
    public Player getCustomPlayer(Player bukkitPlayer) {
        return bukkitPlayer;
    }
}
