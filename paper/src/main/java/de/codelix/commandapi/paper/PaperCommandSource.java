package de.codelix.commandapi.paper;

import de.codelix.commandapi.minecraft.MCCommandSource;
import org.bukkit.command.CommandSender;

/**
 * Author: Placeblock
 */
public class PaperCommandSource<P> extends MCCommandSource<P, CommandSender> {
    public PaperCommandSource(P player, CommandSender sender) {
        super(player, sender);
    }
}
