package de.placeblock.commandapi.bridge.paper;

import de.placeblock.commandapi.bridge.CommandBridge;
import de.placeblock.commandapi.core.CommandAPICommand;
import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.core.context.ParseResults;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractPaperCommandBridge<PL extends JavaPlugin, P> extends Command implements CommandBridge<Player, P, CommandSender, PaperCommandSource<P>>, CommandExecutor, TabCompleter {
    @Getter
    private final CommandAPICommand<PaperCommandSource<P>> commandAPICommand;

    @Getter
    private final PL plugin;

    public AbstractPaperCommandBridge(PL plugin, String label) {
        super(label);
        this.plugin = plugin;

        this.commandAPICommand = new CommandAPICommand<>(label) {
            @Override
            public LiteralArgumentBuilder<PaperCommandSource<P>> generateCommand(LiteralArgumentBuilder<PaperCommandSource<P>> literalArgumentBuilder) {
                return AbstractPaperCommandBridge.this.generateCommand(literalArgumentBuilder);
            }
            @Override
            public boolean hasSourcePermission(PaperCommandSource<P> source, String permission) {
                if (source.getPlayer() != null) {
                    return AbstractPaperCommandBridge.this.hasPermission(source.getPlayer(), permission);
                }
                return true;
            }

            @Override
            public void sendSourceMessage(PaperCommandSource<P> source, TextComponent message) {
                if (source.getPlayer() != null) {
                    AbstractPaperCommandBridge.this.sendMessage(source.getPlayer(), message);
                } else {
                    source.getSender().sendMessage(message);
                }
            }
        };

        this.setAliases(this.commandAPICommand.getCommandNode().getAliases());

        plugin.getServer().getCommandMap().register(label, "commandapi", this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        P lobbyPlayer = null;
        if (sender instanceof Player player) {
            lobbyPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(lobbyPlayer, sender);
        ParseResults<PaperCommandSource<P>> parseResults = this.commandAPICommand.parse(source, label + " " + String.join(" ", args));
        this.commandAPICommand.execute(parseResults);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String buffer = label + " " + String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<PaperCommandSource<P>> parseResults = this.commandAPICommand.parse(new PaperCommandSource<>(customPlayer, sender), buffer);
        return this.commandAPICommand.getSuggestions(parseResults);
    }
}
