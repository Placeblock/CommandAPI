package de.codelix.commandapi.paper;

import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.minecraft.MCCommandBridge;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class AbstractPaperCommandBridge<PL extends JavaPlugin, P> extends Command implements MCCommandBridge<Player, P, CommandSender, PaperCommandSource<P>>, Listener {
    @Getter
    private de.codelix.commandapi.core.Command<PaperCommandSource<P>> command;

    @Getter
    private final PL plugin;
    @Getter
    private final boolean async;

    public AbstractPaperCommandBridge(PL plugin, String label, boolean async) {
        this(plugin, label, async, true);
    }

    public AbstractPaperCommandBridge(PL plugin, String label, boolean async, boolean autoInit) {
        super(label);
        this.plugin = plugin;
        this.async = async;
        if (autoInit) {
            this.init();
        }
    }

    public void init() {
        this.command = new de.codelix.commandapi.core.Command<>(this.getLabel(), this.isAsync()) {
            @Override
            public LiteralCommandNodeBuilder<PaperCommandSource<P>> generateCommand(LiteralCommandNodeBuilder<PaperCommandSource<P>> builder) {
                return AbstractPaperCommandBridge.this.generateCommand(builder);
            }

            @Override
            public boolean hasPermission(PaperCommandSource<P> source, String permission) {
                return AbstractPaperCommandBridge.this.hasPermission(source, permission);
            }

            @Override
            public void sendMessageRaw(PaperCommandSource<P> source, TextComponent message) {
                AbstractPaperCommandBridge.this.sendMessageSource(source, message);
            }
        };
        this.setPermission(this.command.getBase().getPermission());
        this.setAliases(this.command.getBase().getAliases());
    }

    @Override
    public void sendMessage(PaperCommandSource<P> source, TextComponent message) {
        this.command.sendMessage(source, message);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        P lobbyPlayer = null;
        if (sender instanceof Player player) {
            lobbyPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(lobbyPlayer, sender);
        new Thread(() -> {
            List<ParsedCommandBranch<PaperCommandSource<P>>> parseResults = this.command.parse(commandLabel + " " + String.join(" ", args), source);
            ParsedCommandBranch<PaperCommandSource<P>> bestResult = de.codelix.commandapi.core.Command.getBestResult(parseResults);
            if (this.command.isAsync()) {
                this.execute(bestResult, source);
            } else {
                Bukkit.getScheduler().runTask(this.plugin, () -> this.execute(bestResult, source));
            }
        }).start();

        return true;
    }

    private void execute(ParsedCommandBranch<PaperCommandSource<P>> parseResult, PaperCommandSource<P> source) {
        this.command.execute(parseResult, source);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        String buffer = alias + " " + String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(customPlayer, sender);
        List<ParsedCommandBranch<PaperCommandSource<P>>> parseResults = this.command.parse(buffer, source);
        return this.command.getSuggestions(parseResults, source);
    }

    @Override
    public void register() {
        this.plugin.getServer().getCommandMap().register(this.getLabel(), "commandapi", this);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
        CommandMap commandMap = this.plugin.getServer().getCommandMap();
        this.unregister(commandMap);
        Map<String, Command> knownCommands = commandMap.getKnownCommands();
        knownCommands.remove("commandapi:" + this.getLabel());
        knownCommands.remove(this.getLabel());
    }
}
