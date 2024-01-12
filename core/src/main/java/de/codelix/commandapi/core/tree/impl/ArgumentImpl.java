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

public interface ArgumentImpl<T> extends NodeImpl, Argument<T> {
    @Override
    default void parse(ParseContext ctx, ParsedCommand cmd) throws SyntaxException {
        T value = this.getParameter().parse(ctx, cmd);
        cmd.storeArgument(this, value);
    }

    default String getDisplayNameSafe() {
        if (this.getDisplayName() != null) return this.getDisplayName();
        return this.getName();
    }
}
