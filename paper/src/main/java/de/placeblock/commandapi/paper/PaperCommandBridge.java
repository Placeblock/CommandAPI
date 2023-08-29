package de.placeblock.commandapi.paper;

import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class PaperCommandBridge<PL extends JavaPlugin> extends AbstractPaperCommandBridge<PL, Player> {
    public PaperCommandBridge(PL plugin, String label, boolean async) {
        super(plugin, label, async);
    }
    public PaperCommandBridge(PL plugin, String label, boolean async, boolean autoInnit) {
        super(plugin, label, async, autoInnit);
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(Player player, TextComponent message) {
        player.sendMessage(message);
    }

    @Override
    public Player getCustomPlayer(Player bukkitPlayer) {
        return bukkitPlayer;
    }

    public static LiteralTreeCommandBuilder<PaperCommandSource<Player>> literal(final String name) {
        return new LiteralTreeCommandBuilder<>(name);
    }

    public static <T> ParameterTreeCommandBuilder<PaperCommandSource<Player>, T> parameter(final String name, Parameter<PaperCommandSource<Player>, T> parameter) {
        return new ParameterTreeCommandBuilder<>(name, parameter);
    }
}
