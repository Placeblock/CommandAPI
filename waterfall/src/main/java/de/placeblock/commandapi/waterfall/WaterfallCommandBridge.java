package de.placeblock.commandapi.waterfall;

import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class WaterfallCommandBridge<PL extends Plugin> extends AbstractWaterfallCommandBridge<PL, ProxiedPlayer> {

    public WaterfallCommandBridge(PL plugin, String label, boolean async) {
        super(plugin, label, async);
    }

    public WaterfallCommandBridge(PL plugin, String label, boolean async, boolean autoInit) {
        super(plugin, label, async, autoInit);
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


    public static LiteralTreeCommandBuilder<WaterfallCommandSource<ProxiedPlayer>> literal(final String name) {
        return new LiteralTreeCommandBuilder<>(name);
    }

    public static <S> ParameterTreeCommandBuilder<WaterfallCommandSource<ProxiedPlayer>, S> parameter(final String name, Parameter<WaterfallCommandSource<ProxiedPlayer>, S> parameter) {
        return new ParameterTreeCommandBuilder<>(name, parameter);
    }
}
