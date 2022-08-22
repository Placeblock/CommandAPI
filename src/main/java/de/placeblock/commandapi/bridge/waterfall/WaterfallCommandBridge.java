package de.placeblock.commandapi.bridge.waterfall;

import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class WaterfallCommandBridge<PL extends Plugin> extends AbstractWaterfallCommandBridge<PL, ProxiedPlayer> {

    public WaterfallCommandBridge(PL plugin, String label) {
        super(plugin, label);
    }

    @Override
    public boolean hasPermission(ProxiedPlayer customPlayer, String permission) {
        return customPlayer.hasPermission(permission);
    }

    @Override
    public void sendMessage(ProxiedPlayer customPlayer, TextComponent message) {
        customPlayer.sendMessage();
    }

    @Override
    public ProxiedPlayer getCustomPlayer(ProxiedPlayer proxiedPlayer) {
        return proxiedPlayer;
    }
}
