package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class CoreCommand<S> implements Command<S> {
   private final Node<S> rootNode;

    @Override
    public boolean hasPermission(S source, String permission) {
        return true;
    }
}
