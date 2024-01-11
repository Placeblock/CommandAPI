package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.run.RunConsumer;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Attribute;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

public class AttributeImpl<T> extends NodeImpl implements Attribute<T> {
    @Getter
    private final String name;
    @Getter
    private final Parameter<T> parameter;

    public AttributeImpl(String name, Parameter<T> parameter, String displayName, List<Node> children, Permission permission, boolean optional, Collection<RunConsumer> runConsumers) {
        super(displayName, children, permission, optional, runConsumers);
        this.name = name;
        this.parameter = parameter;
    }

    @Override
    public void parse(ParseContext ctx, ParsedCommand cmd) throws SyntaxException {
        T value = this.parameter.parse(ctx, cmd);
        cmd.storeParam(this, value);
    }

    @Override
    public String getDisplayName() {
        if (this.displayName != null) return this.displayName;
        return this.name;
    }
}
