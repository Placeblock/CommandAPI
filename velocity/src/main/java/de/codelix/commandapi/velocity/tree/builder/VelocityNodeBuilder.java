package de.codelix.commandapi.velocity.tree.builder;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.tree.builder.AdventureNodeBuilder;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityNode;

public interface VelocityNodeBuilder<B extends VelocityNodeBuilder<B, R, S, P>, R extends VelocityNode<S, P>, S extends VelocitySource<P>, P> extends AdventureNodeBuilder<B, R, S, P, ConsoleCommandSource> {
}
