package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.minecraft.tree.MinecraftArgumentBuilder;
import de.codelix.commandapi.minecraft.tree.MinecraftLiteralBuilder;

@SuppressWarnings("unused")
public interface MinecraftCommand<S extends MinecraftSource<P, ?>, P> extends Command<MinecraftLiteralBuilder<S, P>, MinecraftArgumentBuilder<?, S, P>, S> {
    void build(MinecraftLiteralBuilder<S, P> builder);

    void register();

    void unregister();
}
