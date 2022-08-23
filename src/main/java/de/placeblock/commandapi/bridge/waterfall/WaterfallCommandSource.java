package de.placeblock.commandapi.bridge.waterfall;

import de.placeblock.commandapi.bridge.CommandSource;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;

@Getter
public class WaterfallCommandSource<P> extends CommandSource<P, CommandSender> {
    public WaterfallCommandSource(P player, CommandSender sender) {
        super(player, sender);
    }
}
