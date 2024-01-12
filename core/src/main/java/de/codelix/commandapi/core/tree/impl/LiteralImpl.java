package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.EOLSyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

public interface LiteralImpl extends NodeImpl, Literal {
    @Override
    default void parse(ParseContext ctx, ParsedCommand parsedCommand) throws SyntaxException {
        String next = ctx.getInput().poll();
        if (next == null) throw new EOLSyntaxException();
        if (!this.getNames().contains(next)) {
            throw new SyntaxException();
        }
    }

    default String getDisplayNameSafe() {
        if (this.getDisplayName() != null) return this.getDisplayName();
        return this.getNames().get(0);
    }
}
