package de.codelix.commandapi.waterfall;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.codelix.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
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
    public boolean hasPermissionPlayer(ProxiedPlayer customPlayer, String permission) {
        return customPlayer.hasPermission(permission);
    }

    @Override
    public void sendMessagePlayer(ProxiedPlayer customPlayer, TextComponent message) {
        customPlayer.sendMessage(BungeeComponentSerializer.get().serialize(message));
    }

    @Override
    public void sendMessageConsole(CommandSender console, TextComponent message) {
        console.sendMessage(BungeeComponentSerializer.get().serialize(message));
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
