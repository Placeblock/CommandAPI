package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.Branch;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.tree.Literal;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;

import java.util.List;

public class LiteralImpl extends NodeImpl implements Literal {
    @Getter
    private final List<String> names;

    public LiteralImpl(List<String> names, String displayName, List<Node> children, Permission permission) {
        super(displayName, children, permission);
        this.names = names;
    }

    @Override
    public void parse(Branch branch) {

    }
}
