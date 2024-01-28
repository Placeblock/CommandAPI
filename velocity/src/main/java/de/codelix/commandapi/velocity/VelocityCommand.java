package de.codelix.commandapi.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.adventure.AdventureCommand;
import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.velocity.tree.builder.VelocityArgumentBuilder;
import de.codelix.commandapi.velocity.tree.builder.VelocityFactory;
import de.codelix.commandapi.velocity.tree.builder.VelocityLiteralBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class VelocityCommand<P, L extends VelocityLiteralBuilder<?, ?, VelocitySource<P>, P>, A extends VelocityArgumentBuilder<?, ?, ?, VelocitySource<P>, P>> implements RawCommand, AdventureCommand<VelocitySource<P>, P, ConsoleCommandSource, AdventureDesign<VelocitySource<P>>, L, A> {
    private final ProxyServer proxy;
    private CommandMeta meta;
    private final String label;
    @Getter
    private Literal<VelocitySource<P>, TextComponent> rootNode;
    @Getter
    @Accessors(fluent = true)
    private final VelocityFactory<L, A, VelocitySource<P>, P> factory;
    @Getter
    private final AdventureDesign<VelocitySource<P>> design;

    public VelocityCommand(ProxyServer proxy, String label, AdventureDesign<VelocitySource<P>> design, VelocityFactory<L, A, VelocitySource<P>, P> factory) {
        this.label = label;
        this.proxy = proxy;
        this.design = design;
        this.factory = factory;
    }

    @Override
    public void execute(Invocation invocation) {
        VelocitySource<P> source = this.getSource(invocation.source());
        List<String> arguments = this.getArguments(invocation);
        if (invocation.arguments().endsWith(" ")) {
            arguments.add(" ");
        }
        this.runSafe(arguments, source);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        VelocitySource<P> source = this.getSource(invocation.source());
        List<String> arguments = this.getArguments(invocation);
        return this.getSuggestions(arguments, source);
    }

    private List<String> getArguments(Invocation invocation) {
        List<String> arguments = new ArrayList<>(List.of(invocation.alias()));
        arguments.addAll(List.of(invocation.arguments().split(" ")));
        return arguments;
    }

    private VelocitySource<P> getSource(CommandSource sender) {
        VelocitySource<P> source;
        if (sender instanceof Player player) {
            P customPlayer = this.getPlayer(player);
            source = this.createSource(customPlayer, null);
        } else if (sender instanceof ConsoleCommandSource consoleCommandSource){
            source = this.createSource(null, consoleCommandSource);
        } else {
            throw new IllegalStateException("Invalid source for command " + this.label);
        }
        return source;
    }

    protected abstract VelocitySource<P> createSource(P player, CommandSource console);

    protected abstract L createLiteralBuilder(String label);

    private void build() {
        L builder = this.createLiteralBuilder(this.label);
        this.build(builder);
        this.rootNode = builder.build();
    }

    @Override
    public void register() {
        if (this.rootNode == null) {
            this.build();
        }
        List<String> names = this.rootNode.getNames();
        List<String> aliases = names.subList(1, names.size());
        CommandManager manager = this.proxy.getCommandManager();
        this.meta = manager.metaBuilder(this.label)
            .aliases(aliases.toArray(String[]::new))
            .plugin(this)
            .build();
        manager.register(this.meta, this);
    }

    @Override
    public void unregister() {
        CommandManager manager = this.proxy.getCommandManager();
        manager.unregister(this.meta);
    }

    protected abstract P getPlayer(Player player);
}
