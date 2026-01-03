package de.codelix.commandapi.paper.dtogenerator;

import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.builder.PaperLiteralBuilder;
import de.codelix.commandapi.paper.tree.builder.impl.DefaultPaperNodeBuilder;
import de.codelix.commandapi.paper.tree.impl.PaperLiteralImpl;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CallbackLiteralBuilder<S extends PaperSource<P>, P> extends DefaultPaperNodeBuilder<CallbackLiteralBuilder<S, P>, CallbackLiteralImpl<S, P>, S, P> implements PaperLiteralBuilder<CallbackLiteralBuilder<S, P>, CallbackLiteralImpl<S, P>, S, P> {

    private final List<String> names;
    private BiConsumer<ParseContext<S, TextComponent>, ParsedCommand<S, TextComponent>> callback;

    public CallbackLiteralBuilder(String name, String... aliases) {
        this.names = new ArrayList<>(List.of(name));
        this.names.addAll(List.of(aliases));
    }

    public CallbackLiteralBuilder<S, P> callback(BiConsumer<ParseContext<S, TextComponent>, ParsedCommand<S, TextComponent>> callback) {
        this.callback = callback;
        return getThis();
    }

    @Override
    public CallbackLiteralBuilder<S, P> alias(String alias) {
        this.names.add(alias);
        return this;
    }
    @Override
    public CallbackLiteralImpl<S, P> build() {
        List<Node<S, TextComponent>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S, TextComponent> child : this.children) {
            children.add(child.build());
        }
        return new CallbackLiteralImpl<>(this.names, this.displayName, this.description, children, this.permission,
            this.unsafePermission, this.optional, this.runConsumers, this.callback);
    }

    @Override
    protected CallbackLiteralBuilder<S, P> getThis() {
        return this;
    }
}
