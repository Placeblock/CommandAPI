package de.codelix.commandapi.velocity.tree;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.tree.AdventureLiteral;
import de.codelix.commandapi.velocity.VelocitySource;

public interface VelocityLiteral<S extends VelocitySource<P>, P> extends VelocityNode<S, P>, AdventureLiteral<S, P, ConsoleCommandSource> {
}
