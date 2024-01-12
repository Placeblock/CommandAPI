package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.tree.builder.LiteralBuilder;

public class CoreLiteralBuilder<S> extends LiteralBuilder<CoreLiteralBuilder<S>, CoreLiteral<S>, S> {
    public CoreLiteralBuilder(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    public CoreLiteral<S> build() {
        return new CoreLiteral<>(this.names, this.displayName, this.buildChildren(), this.permission, this.optional, this.runConsumers);
    }

    @Override
    protected CoreLiteralBuilder<S> getThis() {
        return this;
    }
}
