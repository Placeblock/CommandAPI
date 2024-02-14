package de.codelix.commandapi.velocity.tree;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.tree.AdventureNode;
import de.codelix.commandapi.velocity.VelocitySource;

public interface VelocityNode<S extends VelocitySource<P>, P> extends AdventureNode<S, P, ConsoleCommandSource> {
}
