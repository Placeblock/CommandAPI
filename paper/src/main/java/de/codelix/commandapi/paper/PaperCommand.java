package de.codelix.commandapi.paper;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import de.codelix.commandapi.adventure.AdventureCommand;
import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.paper.tree.builder.PaperFactory;
import de.codelix.commandapi.paper.tree.builder.PaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.builder.PaperLiteralBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.TextComponent;
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

public abstract class PaperCommand<P, L extends PaperLiteralBuilder<?, ?, PaperSource<P>, P>, A extends PaperArgumentBuilder<?, ?, ?, PaperSource<P>, P>> extends BukkitCommand implements AdventureCommand<PaperSource<P>, P, CommandSender, AdventureDesign<PaperSource<P>>, L, A>, Listener {
    private final Plugin plugin;
    @Getter
    private final boolean async;
    @Getter
    private Literal<PaperSource<P>, TextComponent> rootNode;
    @Getter
    @Accessors(fluent = true)
    private final PaperFactory<L, A, PaperSource<P>, P> factory;
    @Getter
    private final AdventureDesign<PaperSource<P>> design;

    public PaperCommand(Plugin plugin, String label, boolean async, AdventureDesign<PaperSource<P>> design, PaperFactory<L, A, PaperSource<P>, P> factory) {
        super(label);
        this.async = async;
        this.plugin = plugin;
        this.design = design;
        this.factory = factory;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NonNull @NotNull String[] args) {
        List<String> arguments = new ArrayList<>(List.of(commandLabel));
        arguments.addAll(List.of(args));
        PaperSource<P> source = this.getSource(sender);
        if (this.isAsync()) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () ->
                this.runSafe(arguments, source));
        } else {
            this.runSafe(arguments, source);
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
            source = this.createSource(customPlayer, null);
        } else {
            source = this.createSource(null, sender);
        }
        return source;
    }

    protected abstract PaperSource<P> createSource(P player, CommandSender console);

    protected abstract L createLiteralBuilder(String label);

    private void build() {
        L builder = this.createLiteralBuilder(this.getLabel());
        this.build(builder);
        this.rootNode = builder.build();
    }

    @Override
    public void register() {
        if (this.rootNode == null) {
            this.build();
            List<String> names = this.rootNode.getNames();
            this.setAliases(names.subList(1, names.size()));
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
