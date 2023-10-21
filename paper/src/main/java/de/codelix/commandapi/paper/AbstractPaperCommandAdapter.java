package de.codelix.commandapi.paper;

import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.minecraft.MCCommandAudience;
import de.codelix.commandapi.minecraft.MCCommandBridge;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
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
public abstract class AbstractPaperCommandAdapter<PL extends JavaPlugin, P> extends Command implements MCCommandBridge<P, MCCommandAudience<P>>, Listener {
    @Getter
    private de.codelix.commandapi.core.Command<MCCommandAudience<P>> command;
    private final CommandDesign design;

    @Getter
    private final PL plugin;
    @Getter
    private final boolean async;

    public AbstractPaperCommandAdapter(PL plugin, String label) {
        this(plugin, label, true);
    }

    public AbstractPaperCommandAdapter(PL plugin, String label, CommandDesign design) {
        this(plugin, label, true, design);
    }

    public AbstractPaperCommandAdapter(PL plugin, String label, boolean async) {
        this(plugin, label, async, true);
    }

    public AbstractPaperCommandAdapter(PL plugin, String label, boolean async, CommandDesign design) {
        this(plugin, label, async, true, design);
    }

    public AbstractPaperCommandAdapter(PL plugin, String label, boolean async, boolean autoInit) {
        this(plugin, label, async, autoInit, de.codelix.commandapi.core.Command.DESIGN);
    }

    public AbstractPaperCommandAdapter(PL plugin, String label, boolean async, boolean autoInit, CommandDesign design) {
        super(label);
        this.plugin = plugin;
        this.async = async;
        this.design = design;
        if (autoInit) {
            this.init();
        }
    }

    public void init() {
        this.command = new de.codelix.commandapi.core.Command<>(this.getLabel(), this.isAsync(), this.design) {
            @Override
            public LiteralCommandNodeBuilder<MCCommandAudience<P>> generateCommand(LiteralCommandNodeBuilder<MCCommandAudience<P>> builder) {
                return AbstractPaperCommandAdapter.this.generateCommand(builder);
            }

            @Override
            public boolean hasPermission(MCCommandAudience<P> source, String permission) {
                return AbstractPaperCommandAdapter.this.hasPermission(source, permission);
            }

            @Override
            public void sendMessage(MCCommandAudience<P> source, TextComponent message) {
                source.sendMessage(message);
            }

            @Override
            public void sendMessageRaw(MCCommandAudience<P> source, TextComponent message) {
                source.sendMessageRaw(message);
            }
        };
        this.setPermission(this.command.getBase().getPermission());
        this.setAliases(this.command.getBase().getAliases());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        MCCommandAudience<P> source = new MCCommandAudience<>(sender, this.command.getPrefix());
        new Thread(() -> {
            List<ParsedCommandBranch<MCCommandAudience<P>>> parseResults = this.command.parse(commandLabel + " " + String.join(" ", args), source);
            ParsedCommandBranch<MCCommandAudience<P>> bestResult = de.codelix.commandapi.core.Command.getBestResult(parseResults);
            if (this.command.isAsync()) {
                this.execute(bestResult, source);
            } else {
                Bukkit.getScheduler().runTask(this.plugin, () -> this.execute(bestResult, source));
            }
        }).start();

        return true;
    }

    public abstract boolean hasPermission(MCCommandAudience<P> source, String permission);

    private void execute(ParsedCommandBranch<MCCommandAudience<P>> parseResult, MCCommandAudience<P> source) {
        this.command.execute(parseResult, source);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        String buffer = alias + " " + String.join(" ", args);
        MCCommandAudience<P> source = new MCCommandAudience<P>(sender, this.command.getPrefix());
        List<ParsedCommandBranch<MCCommandAudience<P>>> parseResults = this.command.parse(buffer, source);
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
