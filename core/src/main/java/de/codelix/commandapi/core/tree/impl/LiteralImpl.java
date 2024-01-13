package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.EOLSyntaxException;
import de.codelix.commandapi.core.exception.InvalidLiteralSyntaxException;
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

public interface LiteralImpl<S> extends NodeImpl<S>, Literal<S> {
    @Override
    default void parse(ParseContext<S> ctx, ParsedCommand<S> parsedCommand) throws SyntaxException {
        String next = ctx.getInput().poll();
        if (next == null) throw new EOLSyntaxException();
        if (!this.getNames().contains(next)) {
            throw new InvalidLiteralSyntaxException();
        }
    }
}
