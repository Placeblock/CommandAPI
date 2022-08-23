package de.placeblock.commandapi.bridge;

import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import net.kyori.adventure.text.TextComponent;

public interface CommandBridge<DP, P, C, S extends CommandSource<P, C>> {
    P getCustomPlayer(DP defaultPlayer);
    void sendMessage(P customPlayer, TextComponent message);
    boolean hasPermission(P customPlayer, String permission);
    LiteralArgumentBuilder<S> generateCommand(LiteralArgumentBuilder<S> literalArgumentBuilder);
}
