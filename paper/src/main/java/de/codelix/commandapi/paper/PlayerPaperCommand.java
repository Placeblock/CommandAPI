package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureDesign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class PlayerPaperCommand extends DefaultPaperCommand<Player> {
    public PlayerPaperCommand(Plugin plugin, String label, boolean async, AdventureDesign<PaperSource<Player>> design) {
        super(plugin, label, async, design);
    }

    public PlayerPaperCommand(Plugin plugin, String label, AdventureDesign<PaperSource<Player>> design) {
        super(plugin, label, design);
    }

    public PlayerPaperCommand(Plugin plugin, String label, boolean async) {
        super(plugin, label, async);
    }

    public PlayerPaperCommand(Plugin plugin, String label) {
        super(plugin, label);
    }

    @Override
    protected PaperSource<Player> createSource(Player player, CommandSender console) {
        return new DefaultPaperSource(player, console);
    }

    @Override
    protected Player getPlayer(Player player) {
        return player;
    }
}
