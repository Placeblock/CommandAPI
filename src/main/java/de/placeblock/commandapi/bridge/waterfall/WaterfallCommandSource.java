package de.placeblock.commandapi.bridge.waterfall;

import de.placeblock.commandapi.bridge.CommandSource;
import net.md_5.bungee.api.CommandSender;

/**
 * Author: Placeblock
 */
public class WaterfallCommandSource<P> extends CommandSource<P, CommandSender> {
    public WaterfallCommandSource(P player, CommandSender sender) {
        super(player, sender);
    }
}
