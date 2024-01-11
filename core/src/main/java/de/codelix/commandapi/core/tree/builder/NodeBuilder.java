package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.run.*;
import de.codelix.commandapi.core.tree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class NodeBuilder<B extends NodeBuilder<B, R>, R extends Node> {

    protected String displayName;
    protected final List<NodeBuilder<?, ?>> children = new ArrayList<>();
    protected Permission permission;
    protected boolean optional = false;
    protected Collection<RunConsumer> runConsumers = new ArrayList<>();

    public B displayName(String displayName) {
        this.displayName = displayName;
        return this.getThis();
    }

    public B then(NodeBuilder<?, ?> child) {
        this.children.add(child);
        return this.getThis();
    }

    public B permission(Permission permission) {
        this.permission = permission;
        return this.getThis();
    }

    public B optional() {
        this.optional = true;
        return this.getThis();
    }

    public B run(RunConsumer1<?> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    protected List<Node> buildChildren() {
        List<Node> children = new ArrayList<>();
        for (NodeBuilder<?, ?> child : this.children) {
            children.add(child.build());
        }
        return children;
    }


    public abstract R build();
    protected abstract B getThis();

}
