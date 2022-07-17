package de.placeblock.commandapi.bungee;

import de.placeblock.commandapi.core.CommandAPICommand;
import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.core.context.ParseResults;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@SuppressWarnings("unused")
public abstract class WaterfallCommandBridge<P> extends Command implements TabExecutor {
    @Getter
    private final CommandAPICommand<WaterfallCommandSource<P>> commandAPICommand;

    public WaterfallCommandBridge(String label) {
        super(label);
        this.commandAPICommand = new CommandAPICommand<>(label) {
            @Override
            public LiteralArgumentBuilder<WaterfallCommandSource<P>> generateCommand() {
                return WaterfallCommandBridge.this.generateCommand();
            }
            @Override
            public boolean hasSourcePermission(WaterfallCommandSource<P> source, String permission) {
                if (source.getPlayer() != null) {
                    return WaterfallCommandBridge.this.hasPermission(source.getPlayer(), permission);
                }
                return true;
            }

            @Override
            public void sendSourceMessage(WaterfallCommandSource<P> source, TextComponent message) {
                if (source.getPlayer() != null) {
                    WaterfallCommandBridge.this.sendMessage(source.getPlayer(), message);
                } else {
                    source.getSender().sendMessage(BungeeComponentSerializer.get().serialize(message));
                }
            }
        };
        Plugin plugin = this.getPlugin();

        //Set aliases
        try {
            Field aliasField = Command.class.getDeclaredField("aliases");
            Field modField = Field.class.getDeclaredField("modifiers");
            aliasField.setAccessible(true);
            modField.setAccessible(true);
            modField.setInt(aliasField, aliasField.getModifiers() &~Modifier.FINAL);
            aliasField.set(this, this.commandAPICommand.getAliases().toArray(new String[0]));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        plugin.getProxy().getPluginManager().registerCommand(this.getPlugin(), this);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        P customPlayer = null;
        if (sender instanceof ProxiedPlayer player) {
            customPlayer = this.getCustomPlayer(player);
        }
        WaterfallCommandSource<P> source = new WaterfallCommandSource<>(customPlayer, sender);
        ParseResults<WaterfallCommandSource<P>> parseResults = this.commandAPICommand.parse(source, String.join(" ", args));
        this.commandAPICommand.execute(parseResults);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        String buffer = String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof ProxiedPlayer player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<WaterfallCommandSource<P>> parseResults = this.commandAPICommand.parse(new WaterfallCommandSource<>(customPlayer, sender), buffer);
        parseResults.getContext().print(0);
        return this.commandAPICommand.getSuggestions(parseResults);
    }

    public abstract LiteralArgumentBuilder<WaterfallCommandSource<P>> generateCommand();
    protected abstract boolean hasPermission(P customPlayer, String permission);
    protected abstract void sendMessage(P customPlayer, TextComponent message);
    protected abstract Plugin getPlugin();
    protected abstract P getCustomPlayer(ProxiedPlayer proxiedPlayer);
}
