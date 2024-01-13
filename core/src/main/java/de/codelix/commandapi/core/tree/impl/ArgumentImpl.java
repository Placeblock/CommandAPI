package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

public interface ArgumentImpl<T, S> extends NodeImpl<S>, Argument<T, S> {
    @Override
    default void parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws SyntaxException {
        T value = this.getParameter().parse(ctx, cmd);
        cmd.storeArgument(this, value);
    }
}
