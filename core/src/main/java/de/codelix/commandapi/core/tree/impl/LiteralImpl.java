package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.InvalidLiteralSyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.tree.Literal;

public interface LiteralImpl<S> extends NodeImpl<S>, Literal<S> {
    @Override
    default void parse(ParseContext<S> ctx, ParsedCommand<S> parsedCommand) throws SyntaxException {
        String next = ctx.getInput().poll();
        if (!this.getNames().contains(next)) {
            throw new InvalidLiteralSyntaxException(this);
        }
    }
}
