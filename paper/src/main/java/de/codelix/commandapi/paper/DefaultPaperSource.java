package de.codelix.commandapi.paper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DefaultPaperSource extends PaperSource<Player> {
    public DefaultPaperSource(Player player, CommandSender console) {
        super(player, console);
    }
}
