package de.placeblock.commandapi.paper;

import de.placeblock.commandapi.bridge.CommandSource;
import org.bukkit.command.CommandSender;

/**
 * Author: Placeblock
 */
public class PaperCommandSource<P> extends CommandSource<P, CommandSender> {
    public PaperCommandSource(P player, CommandSender sender) {
        super(player, sender);
    }
}
