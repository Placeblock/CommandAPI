package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public interface MCCommandBridge<P, S> {
    P getCustomPlayer(S source);
    LiteralCommandNodeBuilder<S> generateCommand(LiteralCommandNodeBuilder<S> builder);
    void init();
    @SuppressWarnings("unused")
    void register();
    @SuppressWarnings("unused")
    void unregister();
}
