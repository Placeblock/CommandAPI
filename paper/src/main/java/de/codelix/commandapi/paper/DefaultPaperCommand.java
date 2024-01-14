package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.adventure.AdventureMessages;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class DefaultPaperCommand extends PaperCommand<Player> {
    public DefaultPaperCommand(Plugin plugin, String label, boolean async, AdventureDesign<PaperSource<Player>> design) {
        super(plugin, label, async, new AdventureDesign<>(new AdventureMessages()));
    }
    public DefaultPaperCommand(Plugin plugin, String label, AdventureDesign<PaperSource<Player>> design) {
        super(plugin, label, true, new AdventureDesign<>(new AdventureMessages()));
    }
    public DefaultPaperCommand(Plugin plugin, String label, boolean async) {
        super(plugin, label, async, new AdventureDesign<>(new AdventureMessages()));
    }
    public DefaultPaperCommand(Plugin plugin, String label) {
        super(plugin, label, true, new AdventureDesign<>(new AdventureMessages()));
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
