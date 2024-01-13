package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.Factory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CoreCommand<S> implements Command<CoreLiteralBuilder<S>, CoreArgumentBuilder<?, S>, S> {
   private final Node<S> rootNode;
   private final CoreFactory<S> factory = new CoreFactory<>();

    @Override
    public Factory<CoreLiteralBuilder<S>, CoreArgumentBuilder<?, S>, S> factory() {
        return this.factory;
    }

    @Override
    public boolean hasPermission(S source, String permission) {
        return true;
    }
}
