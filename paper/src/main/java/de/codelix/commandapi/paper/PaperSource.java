package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureSource;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public abstract class PaperSource<P> extends AdventureSource<P, CommandSender> {
    public PaperSource(P player, CommandSender console) {
        super(player, console);
    }

    @Override
    public void sendMessageConsole(Component message) {
        this.getConsole().sendMessage(message);
    }
}
