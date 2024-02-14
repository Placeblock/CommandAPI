package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureDesign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class PlayerPaperCommand extends DefaultPaperCommand<DefaultPaperSource, Player> {
    public PlayerPaperCommand(Plugin plugin, String label, boolean async, AdventureDesign<DefaultPaperSource> design) {
        super(plugin, label, async, design);
    }

    public PlayerPaperCommand(Plugin plugin, String label, AdventureDesign<DefaultPaperSource> design) {
        super(plugin, label, design);
    }

    public PlayerPaperCommand(Plugin plugin, String label, boolean async) {
        super(plugin, label, async);
    }

    public PlayerPaperCommand(Plugin plugin, String label) {
        super(plugin, label);
    }

    @Override
    protected DefaultPaperSource createSource(Player player, CommandSender console) {
        return new DefaultPaperSource(player, console);
    }

    @Override
    protected Player getPlayer(Player player) {
        return player;
    }
}
