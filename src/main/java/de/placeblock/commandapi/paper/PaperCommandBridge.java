package de.placeblock.commandapi.paper;

import de.placeblock.commandapi.core.CommandAPICommand;
import de.placeblock.commandapi.core.context.ParseResults;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public abstract class PaperCommandBridge<P> extends CommandAPICommand<PaperCommandSource<P>> implements CommandExecutor, TabCompleter {

    public PaperCommandBridge(String label) {
        super(label);
        Server server = this.getPlugin().getServer();
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

    protected abstract boolean hasPermission(P player, String permission);
    protected abstract PluginBase getPlugin();
    protected abstract void sendMessage(P player, TextComponent message);
    protected abstract P getCustomPlayer(Player bukkitPlayer);
}
