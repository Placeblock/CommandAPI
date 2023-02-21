package de.placeblock.commandapi.bridge.paper;

import de.placeblock.commandapi.bridge.CommandBridge;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class AbstractPaperCommandBridge<PL extends JavaPlugin, P> extends Command implements CommandBridge<Player, P, CommandSender, PaperCommandSource<P>> {
    @Getter
    private final de.placeblock.commandapi.core.Command<PaperCommandSource<P>> command;

    @Getter
    private final PL plugin;

    public AbstractPaperCommandBridge(PL plugin, String label, boolean async) {
        super(label);
        this.plugin = plugin;

        this.command = new de.placeblock.commandapi.core.Command<>(label, async) {
            @Override
            public LiteralTreeCommandBuilder<PaperCommandSource<P>> generateCommand(LiteralTreeCommandBuilder<PaperCommandSource<P>> builder) {
                return AbstractPaperCommandBridge.this.generateCommand(builder);
            }

            @Override
            public boolean hasPermission(PaperCommandSource<P> source, String permission) {
                if (source.getPlayer() != null) {
                    return AbstractPaperCommandBridge.this.hasPermission(source.getPlayer(), permission);
                }
                return true;
            }

            @Override
            public void sendMessage(PaperCommandSource<P> source, TextComponent message) {
                if (source.isPlayer()) {
                    AbstractPaperCommandBridge.this.sendMessage(source.getPlayer(), message);
                } else {
                    source.getSender().sendMessage(message);
                }
            }
        };

        this.setAliases(this.command.getBase().getAliases());
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        P lobbyPlayer = null;
        if (sender instanceof Player player) {
            lobbyPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(lobbyPlayer, sender);
        List<ParsedCommand<PaperCommandSource<P>>> parseResults = this.command.parse(commandLabel + " " + String.join(" ", args), source);
        try {
            this.command.execute(de.placeblock.commandapi.core.Command.getBestResult(parseResults, source), source);
        } catch (CommandSyntaxException e) {
            this.command.sendMessage(source, e.getTextMessage());
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        String buffer = alias + " " + String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(customPlayer, sender);
        List<ParsedCommand<PaperCommandSource<P>>> parseResults = this.command.parse(buffer, source);
        return this.command.getSuggestions(parseResults, source);
    }

    @Override
    public void register() {
        this.plugin.getServer().getCommandMap().register(this.getLabel(), "commandapi", this);
    }

    @Override
    public void unregister() {
        CommandMap commandMap = this.plugin.getServer().getCommandMap();
        this.unregister(commandMap);
        Map<String, Command> knownCommands = commandMap.getKnownCommands();
        knownCommands.remove("commandapi:" + this.getLabel());
        knownCommands.remove(this.getLabel());
    }
}
