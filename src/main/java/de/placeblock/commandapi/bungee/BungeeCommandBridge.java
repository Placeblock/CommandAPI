package de.placeblock.commandapi.bungee;

import de.placeblock.commandapi.core.CommandAPICommand;
import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.context.ParseResults;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.InvalidCommandException;
import io.schark.design.Texts;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

@SuppressWarnings("unused")
public abstract class BungeeCommandBridge<P> extends Command implements Listener {
    private final CommandAPICommand<BungeeCommandSource<P>> commandAPICommand;

    public BungeeCommandBridge(String label) {
        super(label);
        this.commandAPICommand = new CommandAPICommand<>(label) {
            @Override
            public LiteralArgumentBuilder<BungeeCommandSource<P>> generateCommand() {
                return BungeeCommandBridge.this.generateCommand();
            }
            @Override
            public boolean hasSourcePermission(BungeeCommandSource<P> source, String permission) {
                if (source.getPlayer() != null) {
                    return BungeeCommandBridge.this.hasPermission(source.getPlayer());
                }
                return true;
            }

            @Override
            public void sendSourceMessage(BungeeCommandSource<P> source, TextComponent message) {
                if (source.getPlayer() != null) {
                    BungeeCommandBridge.this.sendMessage(source.getPlayer(), message);
                } else {
                    source.getSender().sendMessage(BungeeComponentSerializer.get().serialize(message));
                }
            }
        };
        Plugin plugin = this.getPlugin();
        plugin.getProxy().getPluginManager().registerListener(this.getPlugin(), this);
        plugin.getProxy().getPluginManager().registerCommand(this.getPlugin(), this);
    }

    public abstract LiteralArgumentBuilder<BungeeCommandSource<P>> generateCommand();
    public abstract boolean hasPermission(P customPlayer);
    public abstract void sendMessage(P customPlayer, TextComponent message);

    @Override
    public void execute(CommandSender sender, String[] args) {
        P customPlayer = null;
        if (sender instanceof ProxiedPlayer player) {
            customPlayer = this.getCustomPlayer(player);
        }
        BungeeCommandSource<P> source = new BungeeCommandSource<>(customPlayer, sender);
        ParseResults<BungeeCommandSource<P>> parseResults = this.commandAPICommand.parse(source, String.join(" ", args));
        this.commandAPICommand.execute(parseResults);

    }

    public void onTabComplete(TabCompleteEvent event) {
        /*String buffer = event.getCursor().substring(1);
        if (!buffer.split(" ")[0].equals(this.commandAPICommand.getLiteral())) {
            return;
        }
        P customPlayer = null;
        if (sender instanceof ProxiedPlayer player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<BungeeCommandSource<P>> parseResults = this.commandAPICommand.parse(new BungeeCommandSource<>(customPlayer, sender), buffer);
        parseResults.getContext().print(0);
        this.commandAPICommand.getSuggestions(parseResults).thenAccept(suggestions -> {
            event.getSuggestions().addAll(suggestions);
        }).exceptionally(throwable -> {
            event.getSuggestions().addAll(new ArrayList<>());
            return null;
        });*/
    }

    abstract Plugin getPlugin();
    abstract P getCustomPlayer(ProxiedPlayer proxiedPlayer);
}
