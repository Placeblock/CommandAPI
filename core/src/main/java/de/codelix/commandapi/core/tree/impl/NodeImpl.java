package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.tree.Node;

import java.util.ArrayList;
import java.util.List;

public interface NodeImpl<S> extends Node<S> {
    default List<Node<S>> getChildrenOptional() {
        List<Node<S>> children = new ArrayList<>();
        for (Node<S> child : this.getChildren()) {
            children.add(child);
            if (child.isOptional()) {
                children.addAll(child.getChildren());
            }
        }
        return children;
    }

    @Override
    default void parseRecursive(ParseContext<S> ctx, ParsedCommand<S> cmd) throws SyntaxException {
        this.parse(ctx, cmd);
        cmd.addNode(this);
        if (ctx.getInput().isEmpty()) return;

        SyntaxException ex = null;
        for (Node<S> child : this.getChildrenOptional()) {
            try {
                child.parseRecursive(ctx.copy(), cmd);
                return;
            } catch (SyntaxException e) {
                ex = e;
            }
        }
        if (ex != null) {
            throw ex;
        }
    }


}
