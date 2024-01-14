package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.message.CommandDesign;
import de.codelix.commandapi.core.message.CommandMessages;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.builder.Factory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public abstract class CoreCommand<S> implements Command<CoreLiteralBuilder<S>, CoreArgumentBuilder<?, S>, S, String, CommandDesign<String>> {
    private final Node<S> rootNode;
    private final CoreFactory<S> factory = new CoreFactory<>();
    private final CommandDesign<String> design = new CommandDesign<>(new CommandMessages<>());

    @Override
    public Factory<CoreLiteralBuilder<S>, CoreArgumentBuilder<?, S>, S> factory() {
        return this.factory;
    }

    public void sendMessage(S source, String message) {
        System.out.println(message);
    }

    @Override
    public boolean hasPermission(S source, String permission) {
        return true;
    }
}
