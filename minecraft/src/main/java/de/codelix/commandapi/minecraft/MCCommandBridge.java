package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public interface MCCommandBridge<DP, P, C, S extends MCCommandSource<P, C>> {
    P getCustomPlayer(DP defaultPlayer);
    void sendMessagePlayer(P customPlayer, TextComponent message);
    void sendMessageConsole(C sender, TextComponent message);
    boolean hasPermissionPlayer(P customPlayer, String permission);
    default void sendMessage(S source, TextComponent message) {
        if (source.isPlayer()) {
            this.sendMessagePlayer(source.getPlayer(), message);
        } else {
            this.sendMessageConsole(source.getSender(), message);
        }
    }
    default boolean hasPermission(S source, String permission) {
        if (source.isPlayer()) {
            return this.hasPermissionPlayer(source.getPlayer(), permission);
        }
        return true;
    }
    LiteralTreeCommandBuilder<S> generateCommand(LiteralTreeCommandBuilder<S> builder);

    void init();

    @SuppressWarnings("unused")
    void register();
    @SuppressWarnings("unused")
    void unregister();
}
