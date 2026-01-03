package de.codelix.commandapi.paper.dtogenerator;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperArgumentBuilder;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperNodeBuilder;
import de.codelix.commandapi.paper.tree.impl.PaperArgumentImpl;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CallbackArgumentBuilder<T, S extends PaperSource<P>, P> extends DefaultPaperNodeBuilder<CallbackArgumentBuilder<T, S, P>, CallbackArgumentImpl<T, S, P>, S, P> implements PaperArgumentBuilder<T, CallbackArgumentBuilder<T, S, P>, CallbackArgumentImpl<T, S, P>, S, P> {
    private final String name;
    private final Parameter<T, S, TextComponent> parameter;
    private CallbackArgumentImpl.Consumer<T, S, P> callback;
    private T defaultValue;

    public CallbackArgumentBuilder(String name, Parameter<T, S, TextComponent> parameter) {
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public CallbackArgumentBuilder<T, S, P> defaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return getThis();
    }

    public CallbackArgumentBuilder<T, S, P> callback(CallbackArgumentImpl.Consumer<T, S, P> callback) {
        this.callback = callback;
        return getThis();
    }

    @Override
    public CallbackArgumentImpl<T, S, P> build() {
        List<Node<S, TextComponent>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, TextComponent> child : this.children) {
            children.add(child.build());
        }
        return new CallbackArgumentImpl<>(this.name, this.parameter, this.defaultValue, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers, this.callback);
    }
    @Override
    protected CallbackArgumentBuilder<T, S, P> getThis() {
        return this;
    }
}
