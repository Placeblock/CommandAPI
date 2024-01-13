package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.EndOfCommandSyntaxException;
import de.codelix.commandapi.core.exception.NoPermissionException;
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
    default void parseRecursive(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        ParseContext<S> ctxCopy = ctx.copy();
        if (!this.isUnsafePermission() && !ctx.hasPermission(this.getPermission())) {
            cmd.setException(new NoPermissionException(this));
            return;
        }
        try {
            this.parse(ctx, cmd);
            cmd.setException(null);
        } catch (SyntaxException ex) {
            ctx.setInput(ctxCopy.getInput());
            cmd.setException(ex);
            return;
        }
        if (ctx.getInput().isEmpty()) {
            if (ctx.hasPermission(this.getPermission())) {
                cmd.addNode(this);
            } else {
                ctx.setInput(ctxCopy.getInput());
                cmd.setException(new NoPermissionException(this));
            }
            return;
        }
        cmd.addNode(this);
        if (this.getChildrenOptional().size()==0 && !ctx.getInput().isEmpty()) {
            cmd.setException(new EndOfCommandSyntaxException());
        }
        for (Node<S> child : this.getChildrenOptional()) {
            child.parseRecursive(ctx.copy(), cmd);
            if (cmd.getException() == null) break;
        }
    }

}
