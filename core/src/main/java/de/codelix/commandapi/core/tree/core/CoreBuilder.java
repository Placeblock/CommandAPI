package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.builder.ArgumentBuilder;
import de.codelix.commandapi.core.tree.builder.Builder;
import de.codelix.commandapi.core.tree.builder.LiteralBuilder;

public class CoreBuilder implements Builder {
    @Override
    public LiteralBuilder literal(String name, String... aliases) {
        return new LiteralBuilder(name, aliases);
    }

    @Override
    public <T> ArgumentBuilder<T> argument(String name, Parameter<T> parameter) {
        return new ArgumentBuilder<>(name, parameter);
    }
}
