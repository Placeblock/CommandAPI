package de.codelix.commandapi.velocity;

import com.velocitypowered.api.command.*;
import com.velocitypowered.api.proxy.connection.Player;
import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.minecraft.MCCommandBridge;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class AbstractVelocityCommandAdapter<P> extends Command<VelocityCommandSource<P>> implements RawCommand, MCCommandBridge<Player, P, ConsoleCommandSource, VelocityCommandSource<P>> {
    public AbstractVelocityCommandAdapter(String label) {
        super(label);
    }

    public AbstractVelocityCommandAdapter(String label, CommandDesign commandDesign) {
        super(label, commandDesign);
    }

    public AbstractVelocityCommandAdapter(String label, boolean async) {
        super(label, async);
    }

    public AbstractVelocityCommandAdapter(String label, boolean async, CommandDesign commandDesign) {
        super(label, async, commandDesign);
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String arguments = invocation.arguments();
        VelocityCommandSource<P> commandSource = getCommandSource(source);
        if (commandSource == null) return;
        List<String> nodes = new ArrayList<>();
        Collections.addAll(nodes, invocation.alias());
        Collections.addAll(nodes, arguments);
        this.parseAndExecute(String.join(" ", nodes), commandSource);
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource source = invocation.source();
        String arguments = invocation.arguments();
        VelocityCommandSource<P> commandSource = getCommandSource(source);
        if (commandSource == null) return new ArrayList<>();
        List<String> nodes = new ArrayList<>();
        Collections.addAll(nodes, invocation.alias());
        Collections.addAll(nodes, arguments);
        List<ParsedCommandBranch<VelocityCommandSource<P>>> parseResults = this.parse(String.join(" ", nodes), commandSource);
        return this.getSuggestions(parseResults, commandSource);
    }

    @Nullable
    private VelocityCommandSource<P> getCommandSource(CommandSource source) {
        VelocityCommandSource<P> commandSource = null;
        if (source instanceof ConsoleCommandSource consoleCommandSource) {
            commandSource = new VelocityCommandSource<>(null, consoleCommandSource);
        } else if (source instanceof Player player) {
            commandSource = new VelocityCommandSource<>(this.getCustomPlayer(player), null);
        }
        return commandSource;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        CommandSource source = invocation.source();
        VelocityCommandSource<P> commandSource = getCommandSource(source);
        return commandSource != null;
    }

    @Override
    public void sendMessageConsole(ConsoleCommandSource console, TextComponent message) {
        console.sendMessage(message);
    }

    @Override
    public void init() {}

    @Override
    public void register() {
        throw new IllegalStateException("Please register the command using an CommandManager");
    }

    public void register(CommandManager manager) {
        String[] aliases = this.getBase().getAliases().toArray(new String[0]);
        CommandMeta meta = manager.createMetaBuilder(this.getBase().getName()).aliases(aliases).build();
        manager.register(meta, this);
    }

    @Override
    public void unregister() {
        throw new IllegalStateException("Please unregister the command using an CommandManager");
    }

    public void unregister(CommandManager manager) {
        manager.unregister(this.getBase().getName());
    }
}
