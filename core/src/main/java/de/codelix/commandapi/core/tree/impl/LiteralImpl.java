package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.EOLSyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.run.RunConsumer;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

public class LiteralImpl extends NodeImpl implements Literal {
    @Getter
    private final List<String> names;

    public LiteralImpl(List<String> names, String displayName, List<Node> children, Permission permission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, children, permission, optional, runConsumers);
        this.names = names;
    }

    @Override
    public void parse(ParseContext ctx, ParsedCommand parsedCommand) throws SyntaxException {
        String next = ctx.getInput().poll();
        if (next == null) throw new EOLSyntaxException();
        if (!this.names.contains(next)) {
            throw new SyntaxException();
        }
    }

    @Override
    public String getDisplayName() {
        if (this.displayName != null) return this.displayName;
        return this.names.get(0);
    }
}
