package de.placeblock.commandapi.bridge.paper;

import de.placeblock.commandapi.bridge.CommandBridge;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

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

        plugin.getServer().getCommandMap().register(label, "commandapi", this);
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        P lobbyPlayer = null;
        if (sender instanceof Player player) {
            lobbyPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(lobbyPlayer, sender);
        ParseContext<PaperCommandSource<P>> parseResults = this.command.parse(commandLabel + " " + String.join(" ", args), source);
        this.command.execute(parseResults);
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        String buffer = alias + " " + String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseContext<PaperCommandSource<P>> parseResults = this.command.parse(buffer, new PaperCommandSource<>(customPlayer, sender));
        return this.command.getSuggestions(parseResults);
    }

}
