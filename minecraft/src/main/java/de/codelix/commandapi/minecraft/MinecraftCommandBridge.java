package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;

/**
 * Author: Placeblock
 */
public interface MinecraftCommandBridge<P, S> {
    P getCustomPlayer(S source);
    LiteralCommandNodeBuilder<S> generateCommand(LiteralCommandNodeBuilder<S> builder);
    void init();
    @SuppressWarnings("unused")
    void register();
    @SuppressWarnings("unused")
    void unregister();
}
