package de.codelix.commandapi.velocity.tree.builder;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.minecraft.MinecraftFactory;
import de.codelix.commandapi.velocity.VelocitySource;
import net.kyori.adventure.text.TextComponent;

public interface VelocityFactory<S extends VelocitySource<P>, P> extends MinecraftFactory<S, P, ConsoleCommandSource, TextComponent> {
    VelocityLiteralBuilder<?, ?, S, P> literal(String name, String... aliases);

    <T> VelocityArgumentBuilder<T, ?, ?, S, P> argument(String name, Parameter<T, S, TextComponent> parameter);
}
