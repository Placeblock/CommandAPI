package de.codelix.commandapi.velocity;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.adventure.AdventureDesign;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.minecraft.MinecraftCommand;
import de.codelix.commandapi.minecraft.tree.MinecraftFactory;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteralBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public abstract class VelocityCommand<P> implements RawCommand, MinecraftCommand<VelocitySource<P>, P, TextComponent, AdventureDesign<VelocitySource<P>>> {
    private final ProxyServer proxy;
    private CommandMeta meta;
    private final String label;
    @Getter
    private final boolean async;
    @Getter
    private Literal<VelocitySource<P>> rootNode;
    @Getter
    @Accessors(fluent = true)
    private final MinecraftFactory<VelocitySource<P>, P> factory = new MinecraftFactory<>();
    @Getter
    private final AdventureDesign<VelocitySource<P>> design;

    public VelocityCommand(ProxyServer proxy, String label, boolean async, AdventureDesign<VelocitySource<P>> design) {
        this.proxy = proxy;
        this.label = label;
        this.async = async;
        this.design = design;
    }

    @Override
    public void execute(Invocation invocation) {

    }

    @Override
    public void sendMessage(VelocitySource<P> source, TextComponent message) {
        if (source.isPlayer()) {
            this.sendMessagePlayer(source.getPlayer(), message);
        } else {
            source.getConsole().sendMessage(message);
        }
    }

    @Override
    public boolean hasPermission(VelocitySource<P> source, String permission) {
        if (source.isConsole()) return true;
        return this.hasPermissionPlayer(source.getPlayer(), permission);
    }

    private void build() {
        MinecraftLiteralBuilder<VelocitySource<P>, P> builder = new MinecraftLiteralBuilder<>(this.label);
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

    abstract void sendMessagePlayer(P source, TextComponent message);

    abstract boolean hasPermissionPlayer(P player, String permission);
}
