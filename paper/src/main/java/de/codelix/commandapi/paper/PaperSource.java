package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureSource;
import org.bukkit.command.CommandSender;

public class PaperSource<P> extends AdventureSource<P, CommandSender> {
    public PaperSource(P player, CommandSender console) {
        super(player, console);
    }
}
