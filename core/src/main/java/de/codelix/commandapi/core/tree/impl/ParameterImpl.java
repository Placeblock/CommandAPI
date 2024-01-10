package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.Branch;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Parameter;
import lombok.Getter;

import java.util.List;

public class ParameterImpl extends NodeImpl implements Parameter {
    @Getter
    private final String name;

    public ParameterImpl(String name, String displayName, List<Node> children, Permission permission) {
        super(displayName, children, permission);
        this.name = name;
    }

    @Override
    public void parse(Branch branch) {

    }
}
