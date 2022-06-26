package de.placeblock.commandapi.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import de.placeblock.commandapi.core.CommandAPICommand;
import de.placeblock.commandapi.core.context.ParseResults;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public abstract class PaperCommandBridge<P> extends CommandAPICommand<PaperCommandSource<P>> implements CommandExecutor, Listener {

    public PaperCommandBridge(String label) {
        super(label);
        Server server = this.getPlugin().getServer();
        server.getPluginManager().registerEvents(this, this.getPlugin());
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

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer().substring(1);
        if (!buffer.split(" ")[0].equals(this.getName())) {
            return;
        }
        P customPlayer = null;
        CommandSender sender = event.getSender();
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<PaperCommandSource<P>> parseResults = this.parse(new PaperCommandSource<>(customPlayer, sender), buffer);
        this.getSuggestions(parseResults).thenAccept(suggestions -> {
            event.setCompletions(suggestions);
            event.setHandled(true);
        }).exceptionally(throwable -> {
            event.setCompletions(new ArrayList<>());
            event.setHandled(true);
            return null;
        });
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
