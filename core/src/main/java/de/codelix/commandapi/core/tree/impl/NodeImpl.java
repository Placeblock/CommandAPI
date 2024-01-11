package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.run.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public abstract class NodeImpl implements Node {
    @Getter
    protected final String displayName;
    protected final List<Node> children;
    @Getter
    protected final Permission permission;
    @Getter
    protected final boolean optional;
    @Getter
    protected final Collection<RunConsumer> runConsumers;

    public List<Node> getChildren() {
        List<Node> children = new ArrayList<>();
        for (Node child : this.children) {
            children.add(child);
            if (child.isOptional()) {
                children.addAll(child.getChildren());
            }
        }
        return children;
    }

    @Override
    public void parseRecursive(ParseContext ctx, ParsedCommand cmd) throws SyntaxException {
        this.parse(ctx, cmd);
        cmd.addNode(this);
        if (ctx.getInput().isEmpty()) return;

        SyntaxException ex = null;
        for (Node child : this.getChildren()) {
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
