package de.codelix.commandapi.velocity.tree;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.tree.AdventureArgument;
import de.codelix.commandapi.velocity.VelocitySource;

public interface VelocityArgument<T, S extends VelocitySource<P>, P> extends VelocityNode<S, P>, AdventureArgument<T, S, P, ConsoleCommandSource> {
}
