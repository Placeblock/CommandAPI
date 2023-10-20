package de.codelix.commandapi.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.minecraft.MCCommandBridge;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractVelocityCommandAdapter<P> extends Command<CommandSource> implements RawCommand, MCCommandBridge<Player, P, ConsoleCommandSource, CommandSource>  {

    public AbstractVelocityCommandAdapter(String label, boolean async, CommandDesign commandDesign) {
        super(label, async, commandDesign);
    }

    @Override
    public void execute(Invocation invocation) {
        Audience.audience(invocation.source());
        invocation.source()
        this.parseAndExecute(invocation.alias() + invocation.arguments(), invocation.source());
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        List<ParsedCommandBranch<CommandSource>> parsed = this.parse(invocation.alias() + invocation.arguments(), invocation.source());
        return this.getSuggestions(parsed, invocation.source());
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        List<ParsedCommandBranch<CommandSource>> parsed = this.parse(invocation.alias() + invocation.arguments(), invocation.source());
        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
        completableFuture.complete(this.getSuggestions(parsed, invocation.source()));
        return completableFuture;
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return this.hasPermission(invocation.source());
    }

    @Override
    public void init() {

    }

    @Override
    public void register() {

    }

    @Override
    public void unregister() {

    }

    @Override
    public void sendMessage(CommandSource source, TextComponent message) {
        source.sendMessage(message);
    }

    @Override
    public boolean hasPermissionPlayer(P customPlayer, String permission) {
        return false;
    }

    @Override
    public boolean hasPermission(CommandSource source, String permission) {
        return source.hasPermission(permission);
    }
}
