package de.codelix.commandapi.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftCommand;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteralBuilder;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PaperCommand<P> extends BukkitCommand implements MinecraftCommand<PaperSource<P>, P>, Listener {
    private final Plugin plugin;
    @Getter
    private final boolean async;
    @Getter
    private Node<PaperSource<P>> rootNode;

    public PaperCommand(Plugin plugin, String label, boolean asnyc) {
        super(label);
        this.async = asnyc;
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NonNull @NotNull String[] args) {
        return false;
    }

    @EventHandler
    public void on(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer();
        List<String> args = new ArrayList<>(List.of(buffer.split(" ")));
        if (buffer.endsWith(" ")) {
            args.add("");
        }
        CommandSender sender = event.getSender();
        PaperSource<P> source;
        if (sender instanceof Player player) {
            P customPlayer = this.getPlayer(player);
            source = new PaperSource<>(customPlayer, null);
        } else {
            source = new PaperSource<>(null, sender);
        }
        this.getSuggestions(args, source);
    }

    private void build() {
        String[] aliases = this.getAliases().toArray(String[]::new);
        MinecraftLiteralBuilder<PaperSource<P>, P> builder = new MinecraftLiteralBuilder<>(this.getLabel(), aliases);
        this.build(builder);
        this.rootNode = builder.build();
    }

    @Override
    public void register() {
        if (this.rootNode == null) {
            this.build();
        }
        Server server = this.plugin.getServer();
        CommandMap commandMap = server.getCommandMap();
        commandMap.register(this.getLabel(), "commandapi", this);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        this.sync();
    }

    public void unregister() {
        CommandMap commandMap = this.plugin.getServer().getCommandMap();
        this.unregister(commandMap);
        Map<String, Command> knownCommands = commandMap.getKnownCommands();
        knownCommands.remove("commandapi:" + this.getLabel());
        knownCommands.remove(this.getLabel());
        HandlerList.unregisterAll(this);
        this.sync();
    }

    private void sync() {
        Server server = this.plugin.getServer();
        try {
            server.getClass().getMethod("syncCommands").invoke(server);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract P getPlayer(Player player);
}
