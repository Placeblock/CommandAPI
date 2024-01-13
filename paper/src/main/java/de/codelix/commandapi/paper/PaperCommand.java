package de.codelix.commandapi.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.minecraft.MinecraftCommand;
import de.codelix.commandapi.minecraft.tree.MinecraftFactory;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteralBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
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
import java.util.concurrent.ExecutionException;

public abstract class PaperCommand<P> extends BukkitCommand implements MinecraftCommand<PaperSource<P>, P>, Listener {
    private final Plugin plugin;
    @Getter
    private final boolean async;
    @Getter
    private Node<PaperSource<P>> rootNode;
    @Getter
    @Accessors(fluent = true)
    private final MinecraftFactory<PaperSource<P>, P> factory = new MinecraftFactory<>();

    public PaperCommand(Plugin plugin, String label, boolean asnyc) {
        super(label);
        this.async = asnyc;
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NonNull @NotNull String[] args) {
        List<String> arguments = new ArrayList<>(List.of(commandLabel));
        arguments.addAll(List.of(args));
        PaperSource<P> source = this.getSource(sender);
        try {
            this.run(arguments, source);
        } catch (SyntaxException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @EventHandler
    public void on(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer().substring(1);
        List<String> args = new ArrayList<>(List.of(buffer.split(" ")));
        if (buffer.endsWith(" ")) {
            args.add("");
        }
        CommandSender sender = event.getSender();
        PaperSource<P> source = this.getSource(sender);
        try {
            List<String> suggestions = this.getSuggestions(args, source).get();
            List<AsyncTabCompleteEvent.Completion> completions = suggestions.stream().map(AsyncTabCompleteEvent.Completion::completion).toList();
            event.completions(completions);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private PaperSource<P> getSource(CommandSender sender) {
        PaperSource<P> source;
        if (sender instanceof Player player) {
            P customPlayer = this.getPlayer(player);
            source = new PaperSource<>(customPlayer, null);
        } else {
            source = new PaperSource<>(null, sender);
        }
        return source;
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

    @Override
    public boolean hasPermission(PaperSource<P> source, String permission) {
        if (source.isConsole()) return true;
        return this.hasPermissionPlayer(source.getPlayer(), permission);
    }

    abstract boolean hasPermissionPlayer(P player, String permission);
}
