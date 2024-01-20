package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureSource;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;

public abstract class PaperSource<P> extends AdventureSource<P, CommandSender> {
    public PaperSource(P player, CommandSender console) {
        super(player, console);
    }

    public abstract void sendMessagePlayer(TextComponent message);
}
