package de.placeblock.commandapi.bridge.waterfall;

import de.placeblock.commandapi.CommandAPI;
import de.placeblock.commandapi.bridge.CommandBridge;
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
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractWaterfallCommandBridge<PL extends Plugin, P> extends Command implements CommandBridge<ProxiedPlayer, P, CommandSender, WaterfallCommandSource<P>>, TabExecutor {
    @Getter
    private final CommandAPICommand<WaterfallCommandSource<P>> commandAPICommand;

    @Getter
    private final PL plugin;

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

    public AbstractWaterfallCommandBridge(PL plugin, String label) {
        super(label);
        this.plugin = plugin;
        this.commandAPICommand = new CommandAPICommand<>(label) {
            @Override
            public LiteralArgumentBuilder<WaterfallCommandSource<P>> generateCommand(LiteralArgumentBuilder<WaterfallCommandSource<P>> literalArgumentBuilder) {
                return AbstractWaterfallCommandBridge.this.generateCommand(literalArgumentBuilder.setAsync(true, true));
            }
            @Override
            public boolean hasSourcePermission(WaterfallCommandSource<P> source, String permission) {
                if (source.getPlayer() != null) {
                    return AbstractWaterfallCommandBridge.this.hasPermission(source.getPlayer(), permission);
                }
                return true;
            }

            @Override
            public void sendSourceMessage(WaterfallCommandSource<P> source, TextComponent message) {
                if (source.getPlayer() != null) {
                    AbstractWaterfallCommandBridge.this.sendMessage(source.getPlayer(), message);
                } else {
                    source.getSender().sendMessage(BungeeComponentSerializer.get().serialize(message));
                }
            }
        };

        //Set aliases
        try {
            Field aliasField = Command.class.getDeclaredField("aliases");
            long fieldOffset = unsafe.objectFieldOffset(aliasField);
            unsafe.putObject(this, fieldOffset, this.commandAPICommand.getCommandNode().getAliases().toArray(new String[0]));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Registered Waterfall Command with Aliases: " + this.commandAPICommand.getCommandNode().getAliases());
            System.out.println("The following Aliases are registered: " + Arrays.toString(this.getAliases()));
        }

        plugin.getProxy().getPluginManager().registerCommand(this.plugin, this);
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
        ParseResults<WaterfallCommandSource<P>> parseResults = this.commandAPICommand.parse(source, String.join(" ", nodes));
        this.commandAPICommand.execute(parseResults);
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
        ParseResults<WaterfallCommandSource<P>> parseResults = this.commandAPICommand.parse(new WaterfallCommandSource<>(customPlayer, sender), buffer);
        parseResults.getContext().print(0);
        return this.commandAPICommand.getSuggestions(parseResults);
    }

}
