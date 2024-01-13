package de.codelix.commandapi.paper;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
}
