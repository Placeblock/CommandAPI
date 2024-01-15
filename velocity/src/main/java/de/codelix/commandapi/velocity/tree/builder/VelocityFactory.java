package de.codelix.commandapi.velocity.tree.builder;

import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.velocity.VelocitySource;

public interface VelocityFactory<L extends VelocityLiteralBuilder<?, ?, S, P>, A extends VelocityArgumentBuilder<?, ?, ?, S, P>, S extends VelocitySource<P>, P> extends Factory<L, A, S> {
}
