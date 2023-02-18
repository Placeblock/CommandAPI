package de.placeblock.commandapi.bridge;

import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public interface CommandBridge<DP, P, C, S extends CommandSource<P, C>> {
    P getCustomPlayer(DP defaultPlayer);
    void sendMessage(P customPlayer, TextComponent message);
    boolean hasPermission(P customPlayer, String permission);
    LiteralTreeCommandBuilder<S> generateCommand(LiteralTreeCommandBuilder<S> builder);

    void register();
    void unregister();
}
