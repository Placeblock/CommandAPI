package de.codelix.commandapi.waterfall;

import de.codelix.commandapi.minecraft.MCCommandSource;
import net.md_5.bungee.api.CommandSender;

/**
 * Author: Placeblock
 */
public class WaterfallCommandSource<P> extends MCCommandSource<P, CommandSender> {
    public WaterfallCommandSource(P player, CommandSender sender) {
        super(player, sender);
    }
}
