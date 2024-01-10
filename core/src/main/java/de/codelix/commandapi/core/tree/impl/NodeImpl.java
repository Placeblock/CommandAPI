package de.codelix.commandapi.core.tree.impl;

import de.codelix.commandapi.core.Branch;
import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class NodeImpl implements Node {
    @Getter
    private final String displayName;
    @Getter
    private final List<Node> children;
    @Getter
    private final Permission permission;

    @Override
    public List<Branch> parseRecursive(Branch branch) {
        return null;
    }
}
