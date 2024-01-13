package de.codelix.commandapi.paper;

import de.codelix.commandapi.minecraft.MinecraftSource;
import org.bukkit.command.CommandSender;

public class PaperSource<P> extends MinecraftSource<P, CommandSender> {
    public PaperSource(P player, CommandSender console) {
        super(player, console);
    }
}
