package de.codelix.commandapi.paper.tree;

import de.codelix.commandapi.core.tree.builder.Factory;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.builder.PaperLiteralBuilder;

public interface PaperFactory<L extends PaperLiteralBuilder<?, ?, S, P>, A extends PaperArgumentBuilder<?, ?, ?, S, P>, S extends PaperSource<P>, P> extends Factory<L, A, S> {
}
