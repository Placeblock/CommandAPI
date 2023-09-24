package de.codelix.commandapi.waterfall;

import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.core.tree.builder.ParameterCommandNodeBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class WaterfallCommandAdapter<PL extends Plugin> extends AbstractWaterfallCommandAdapter<PL, ProxiedPlayer> {

    public WaterfallCommandAdapter(PL plugin, String label) {
        this(plugin, label, true);
    }

    public WaterfallCommandAdapter(PL plugin, String label, CommandDesign design) {
        this(plugin, label, true, design);
    }

    public WaterfallCommandAdapter(PL plugin, String label, boolean async) {
        this(plugin, label, async, true);
    }

    public WaterfallCommandAdapter(PL plugin, String label, boolean async, CommandDesign design) {
        this(plugin, label, async, true, design);
    }

    public WaterfallCommandAdapter(PL plugin, String label, boolean async, boolean autoInit) {
        this(plugin, label, async, autoInit, de.codelix.commandapi.core.Command.DESIGN);
    }

    public WaterfallCommandAdapter(PL plugin, String label, boolean async, boolean autoInit, CommandDesign design) {
        super(plugin, label, async, autoInit, design);
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


    public static LiteralCommandNodeBuilder<WaterfallCommandSource<ProxiedPlayer>> literal(final String name) {
        return new LiteralCommandNodeBuilder<>(name);
    }

    public static <S> ParameterCommandNodeBuilder<WaterfallCommandSource<ProxiedPlayer>, S> parameter(final String name, Parameter<WaterfallCommandSource<ProxiedPlayer>, S> parameter) {
        return new ParameterCommandNodeBuilder<>(name, parameter);
    }
}
