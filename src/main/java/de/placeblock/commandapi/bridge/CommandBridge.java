package de.placeblock.commandapi.bridge;

import net.kyori.adventure.text.TextComponent;

public interface CommandBridge<DP, P> {
    P getCustomPlayer(DP defaultPlayer);
    void sendMessage(P customPlayer, TextComponent message);
    boolean hasPermission(P customPlayer, String permission);
}
