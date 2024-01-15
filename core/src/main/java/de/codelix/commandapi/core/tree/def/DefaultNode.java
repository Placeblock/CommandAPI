package de.codelix.commandapi.core.tree.def;

import de.codelix.commandapi.core.exception.EndOfCommandParseException;
import de.codelix.commandapi.core.exception.NoPermissionParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Node;

public interface DefaultNode<S> extends Node<S> {
    @Override
    default void parseRecursive(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        if (!this.isUnsafePermission() && !ctx.hasPermission(this.getPermission())) {
            cmd.setException(new NoPermissionParseException(this));
            return;
        }
        ParseContext<S> ctxCopy = ctx.copy();
        try {
            this.parse(ctx, cmd);
            cmd.setException(null);
        } catch (ParseException ex) {
            ctx.setInput(ctxCopy.getInput());
            cmd.setException(ex);
            return;
        }
        if (ctx.getInput().isEmpty()) {
            if (ctx.hasPermission(this.getPermission())) {
                cmd.addNode(this);
            } else {
                ctx.setInput(ctxCopy.getInput());
                cmd.setException(new NoPermissionParseException(this));
            }
            return;
        }
        cmd.addNode(this);
        if (this.getChildrenOptional().size()==0 && !ctx.getInput().isEmpty()) {
            cmd.setException(new EndOfCommandParseException());
        }
        for (Node<S> child : this.getChildrenOptional()) {
            child.parseRecursive(ctx, cmd);
            if (cmd.getException() == null) break;
        }
    }

}
