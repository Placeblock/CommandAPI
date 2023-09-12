package de.codelix.commandapi.paper;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.core.tree.builder.ParameterCommandNodeBuilder;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
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
    public boolean hasPermissionPlayer(Player player, String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessagePlayer(Player player, TextComponent message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessageConsole(CommandSender console, TextComponent message) {
        console.sendMessage(message);
    }

    @Override
    public Player getCustomPlayer(Player bukkitPlayer) {
        return bukkitPlayer;
    }

    public static LiteralCommandNodeBuilder<PaperCommandSource<Player>> literal(final String name) {
        return new LiteralCommandNodeBuilder<>(name);
    }

    public static <T> ParameterCommandNodeBuilder<PaperCommandSource<Player>, T> parameter(final String name, Parameter<PaperCommandSource<Player>, T> parameter) {
        return new ParameterCommandNodeBuilder<>(name, parameter);
    }
}
