package de.codelix.commandapi.velocity.tree.builder;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.tree.builder.AdventureArgumentBuilder;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityArgument;

public interface VelocityArgumentBuilder<T, B extends VelocityArgumentBuilder<T, B, R, S, P>, R extends VelocityArgument<T, S, P>, S extends VelocitySource<P>, P> extends VelocityNodeBuilder<B, R, S, P>, AdventureArgumentBuilder<T, B, R, S, P, ConsoleCommandSource> {
}
