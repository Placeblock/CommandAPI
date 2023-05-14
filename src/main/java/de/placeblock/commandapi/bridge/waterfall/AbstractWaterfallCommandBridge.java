package de.placeblock.commandapi.bridge.waterfall;

import de.placeblock.commandapi.bridge.CommandBridge;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public abstract class AbstractWaterfallCommandBridge<PL extends Plugin, P> extends Command implements CommandBridge<ProxiedPlayer, P, CommandSender, WaterfallCommandSource<P>>, TabExecutor {
    @Getter
    private de.placeblock.commandapi.core.Command<WaterfallCommandSource<P>> command;

    @Getter
    private final PL plugin;
    @Getter
    private boolean async;

    private static Unsafe unsafe;


    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public AbstractWaterfallCommandBridge(PL plugin, String label, boolean async) {
        this(plugin, label, async, true);
    }

    public AbstractWaterfallCommandBridge(PL plugin, String label, boolean async, boolean autoInit) {
        super(label);
        this.plugin = plugin;
        this.async = async;
        if (autoInit) {
            this.init();
        }
    }

    public void init() {
        this.command = new de.placeblock.commandapi.core.Command<>(this.getName(), this.isAsync()) {
            @Override
            public LiteralTreeCommandBuilder<WaterfallCommandSource<P>> generateCommand(LiteralTreeCommandBuilder<WaterfallCommandSource<P>> builder) {
                return AbstractWaterfallCommandBridge.this.generateCommand(builder);
            }

            @Override
            public boolean hasPermission(WaterfallCommandSource<P> source, String permission) {
                if (source.getPlayer() != null) {
                    return AbstractWaterfallCommandBridge.this.hasPermission(source.getPlayer(), permission);
                }
                return true;
            }

            @Override
            public void sendMessage(WaterfallCommandSource<P> source, TextComponent message) {
                if (source.isPlayer()) {
                    AbstractWaterfallCommandBridge.this.sendMessage(source.getPlayer(), message);
                } else {
                    source.getSender().sendMessage(BungeeComponentSerializer.get().serialize(message));
                }
            }
        };

        //Set aliases and permission
        try {
            Field aliasField = Command.class.getDeclaredField("aliases");
            long fieldOffset = unsafe.objectFieldOffset(aliasField);
            unsafe.putObject(this, fieldOffset, this.command.getBase().getAliases().toArray(new String[0]));

            Field permissionField = Command.class.getDeclaredField("permission");
            long permissionFieldOffset = unsafe.objectFieldOffset(permissionField);
            unsafe.putObject(this, permissionFieldOffset, this.command.getBase().getPermission());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        P customPlayer = null;
        if (sender instanceof ProxiedPlayer player) {
            customPlayer = this.getCustomPlayer(player);
        }
        WaterfallCommandSource<P> source = new WaterfallCommandSource<>(customPlayer, sender);
        List<String> nodes = new ArrayList<>();
        Collections.addAll(nodes, this.getName());
        Collections.addAll(nodes, args);
        List<ParsedCommand<WaterfallCommandSource<P>>> parseResults = this.command.parse(String.join(" ", nodes), source);
        ParsedCommand<WaterfallCommandSource<P>> bestResult = de.placeblock.commandapi.core.Command.getBestResult(parseResults);
        this.command.execute(bestResult, source);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> nodes = new ArrayList<>();
        Collections.addAll(nodes, this.getName());
        Collections.addAll(nodes, args);
        String buffer = String.join(" ", nodes);
        P customPlayer = null;
        if (sender instanceof ProxiedPlayer player) {
            customPlayer = this.getCustomPlayer(player);
        }
        WaterfallCommandSource<P> source = new WaterfallCommandSource<>(customPlayer, sender);
        List<ParsedCommand<WaterfallCommandSource<P>>> parseResults = this.command.parse(buffer, source);
        return this.command.getSuggestions(parseResults, source);
    }

    @Override
    public void register() {
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, this);
    }

    @Override
    public void unregister() {
        this.plugin.getProxy().getPluginManager().unregisterCommand(this);
    }
}
