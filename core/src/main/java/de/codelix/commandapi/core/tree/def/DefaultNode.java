package de.codelix.commandapi.core.tree.def;

import de.codelix.commandapi.core.exception.EndOfCommandParseException;
import de.codelix.commandapi.core.exception.NoPermissionParseException;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;

import java.util.List;

public interface DefaultNode<S extends Source<M>, M> extends Node<S, M> {
    @Override
    default void parseRecursive(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        if (!this.isUnsafePermission() && !ctx.hasPermission(this.getPermission())) {
            cmd.setException(new NoPermissionParseException(this));
            return;
        }
        ParseContext<S, M> ctxCopy = ctx.copy();
        try {
            this.parse(ctx, cmd);
            cmd.setException(null);
            ParseContext<S, M> parseCopy = ctxCopy.copy();
            parseCopy.getInput().removeAll(ctx.getInput());
            cmd.storeParsed(this, parseCopy.getRemaining());
        } catch (ParseException ex) {
            ctx.setInput(ctxCopy.getInput());
            cmd.setException(ex);
            return;
        }
        if (ctx.getInput().isEmpty() || ctx.getInput().peek().isEmpty()) {
            if (ctx.hasPermission(this.getPermission())) {
                cmd.addNode(this);
            } else {
                ctx.setInput(ctxCopy.getInput());
                cmd.setException(new NoPermissionParseException(this));
            }
            return;
        }
        cmd.addNode(this);
        List<Node<S, M>> parseChildren = this.getParseChildren(ctx, cmd);
        if (parseChildren.isEmpty() && !ctx.getInput().isEmpty()) {
            cmd.setException(new EndOfCommandParseException());
        }
        for (Node<S, M> child : parseChildren) {
            child.parseRecursive(ctx, cmd);
            if (cmd.getException() == null) break;
        }
    }

}
