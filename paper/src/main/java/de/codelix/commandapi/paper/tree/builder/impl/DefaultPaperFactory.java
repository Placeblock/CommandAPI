package de.codelix.commandapi.paper.tree.builder.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperFactory;
import net.kyori.adventure.text.TextComponent;

public class DefaultPaperFactory<S extends PaperSource<P>, P> implements PaperFactory<DefaultPaperLiteralBuilder<S, P>, DefaultPaperArgumentBuilder<?, S, P>, S, P> {
    @Override
    public DefaultPaperLiteralBuilder<S, P> literal(String name, String... aliases) {
        return new DefaultPaperLiteralBuilder<>(name, aliases);
    }

    @Override
    public <T> DefaultPaperArgumentBuilder<T, S, P> argument(String name, Parameter<T, S, TextComponent> parameter) {
        return new DefaultPaperArgumentBuilder<>(name, parameter);
    }
}
