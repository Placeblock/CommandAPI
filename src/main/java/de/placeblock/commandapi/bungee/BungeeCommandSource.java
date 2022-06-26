package de.placeblock.commandapi.bungee;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;

@Getter
@RequiredArgsConstructor
public class BungeeCommandSource<P> {
    private final P player;
    private final CommandSender sender;
}
