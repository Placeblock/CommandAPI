package de.placeblock.commandapi.bridge.paper;

import de.placeblock.commandapi.bridge.CommandBridge;
import de.placeblock.commandapi.core.CommandAPICommand;
import de.placeblock.commandapi.core.context.ParseResults;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class AbstractPaperCommandBridge<PL extends JavaPlugin, P> extends CommandAPICommand<PaperCommandSource<P>> implements CommandBridge<Player, P>, CommandExecutor, TabCompleter {
    @Getter
    private final PL plugin;

    public AbstractPaperCommandBridge(PL plugin, String label) {
        super(label);
        this.plugin = plugin;
        Server server = plugin.getServer();
        Objects.requireNonNull(server.getPluginCommand(label)).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        P lobbyPlayer = null;
        if (sender instanceof Player player) {
            lobbyPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(lobbyPlayer, sender);
        ParseResults<PaperCommandSource<P>> parseResults = this.parse(source, label + " " + String.join(" ", args));
        this.execute(parseResults);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String buffer = label + " " + String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<PaperCommandSource<P>> parseResults = this.parse(new PaperCommandSource<>(customPlayer, sender), buffer);
        return this.getSuggestions(parseResults);
    }

    @Override
    public boolean hasSourcePermission(PaperCommandSource<P> source, String permission) {
        if (source.getPlayer() != null) {
            return this.hasPermission(source.getPlayer(), permission);
        }
        return true;
    }

    @Override
    public void sendSourceMessage(PaperCommandSource<P> source, TextComponent message) {
        if (source.getPlayer() != null) {
            this.sendMessage(source.getPlayer(), message);
        } else {
            source.getSender().sendMessage(message);
        }
    }

}
