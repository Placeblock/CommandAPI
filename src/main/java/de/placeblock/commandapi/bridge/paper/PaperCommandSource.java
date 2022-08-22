package de.placeblock.commandapi.bridge.paper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@Getter
@RequiredArgsConstructor
public class PaperCommandSource<P> {
    private final P player;
    private final CommandSender sender;
}
