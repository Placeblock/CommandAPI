package de.codelix.commandapi.velocity.tree.builder;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.tree.builder.AdventureLiteralBuilder;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.VelocityLiteral;

public interface VelocityLiteralBuilder<B extends VelocityLiteralBuilder<B, R, S, P>, R extends VelocityLiteral<S, P>, S extends VelocitySource<P>, P> extends VelocityNodeBuilder<B, R, S, P>, AdventureLiteralBuilder<B, R, S, P, ConsoleCommandSource> {
}
