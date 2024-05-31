package de.codelix.commandapi.core.tree.builder.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.impl.NodeImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DefaultNodeBuilder<B extends DefaultNodeBuilder<B, R, S, M>, R extends NodeImpl<S, M>, S extends Source<M>, M> implements NodeBuilder<B, R , S, M> {
    protected String displayName;
    protected String description;
    protected List<NodeBuilder<?, ?, S, M>> children = new ArrayList<>();
    protected String permission;
    protected boolean unsafePermission = false;
    protected boolean optional = false;
    protected Collection<RunConsumer> runConsumers = new ArrayList<>();

    @Override
    public B displayName(String displayName) {
        this.displayName = displayName;
        return this.getThis();
    }

    @Override
    public B description(String description) {
        this.description = description;
        return this.getThis();
    }

    @Override
    public B then(NodeBuilder<?, ?, S, M> child) {
        this.children.add(child);
        return this.getThis();
    }

    @Override
    public B permission(String permission) {
        this.permission = permission;
        return this.getThis();
    }

    @Override
    public B unsafePermission() {
        this.unsafePermission = true;
        return this.getThis();
    }

    @Override
    public B optional() {
        this.optional = true;
        return this.getThis();
    }

    @Override
    public B runNative(RunConsumer.RC<S, M> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public B run(RunConsumer.RC0<S, M> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1> B run(RunConsumer.RC1<S, M, T1> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2> B run(RunConsumer.RC2<S, M, T1, T2> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }
    @Override
    public <T1, T2, T3> B run(RunConsumer.RC3<S, M, T1, T2, T3> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3, T4> B run(RunConsumer.RC4<S, M, T1, T2, T3, T4> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, M, T1, T2, T3, T4, T5> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, M, T1, T2, T3, T4, T5, T6> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    protected abstract B getThis();
}
