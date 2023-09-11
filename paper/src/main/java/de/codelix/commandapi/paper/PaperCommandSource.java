package de.codelix.commandapi.paper;

import de.codelix.commandapi.bridge.CommandSource;
import org.bukkit.command.CommandSender;

/**
 * Author: Placeblock
 */
public class PaperCommandSource<P> extends CommandSource<P, CommandSender> {
    public PaperCommandSource(P player, CommandSender sender) {
        super(player, sender);
    }
}
