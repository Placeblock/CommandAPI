package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class NodeBuilder<B extends NodeBuilder<B, R, S>, R extends Node<S>, S> {

    protected String displayName;
    protected final List<NodeBuilder<?, ?, S>> children = new ArrayList<>();
    protected Permission permission;
    protected boolean optional = false;
    protected Collection<RunConsumer<S>> runConsumers = new ArrayList<>();

    public B displayName(String displayName) {
        this.displayName = displayName;
        return this.getThis();
    }

    public B then(NodeBuilder<?, ?, S> child) {
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

    public B runNative(RunConsumer.RC<S> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public B run(RunConsumer.RC0<S> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1> B run(RunConsumer.RC1<S, T1> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2> B run(RunConsumer.RC2<S, T1, T2> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3> B run(RunConsumer.RC3<S, T1, T2, T3> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4> B run(RunConsumer.RC4<S, T1, T2, T3, T4> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, T1, T2, T3, T4, T5> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, T1, T2, T3, T4, T5, T6> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    protected List<Node<S>> buildChildren() {
        List<Node<S>> children = new ArrayList<>();
        for (NodeBuilder<?, ?, S> child : this.children) {
            children.add(child.build());
        }
        return children;
    }


    public abstract R build();
    protected abstract B getThis();

}
