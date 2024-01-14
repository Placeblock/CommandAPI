package de.codelix.commandapi.core.tree.core;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class CoreNodeBuilder<B extends CoreNodeBuilder<B, R, S>, R extends CoreNode<S>, S> implements NodeBuilder<B, R, S> {
    protected String displayName;
    protected String description;
    protected List<NodeBuilder<?, ?, S>> children = new ArrayList<>();
    protected String permission;
    protected boolean optional = false;
    protected boolean unsafePermission = false;
    protected Collection<RunConsumer<S>> runConsumers = new ArrayList<>();

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
    public B then(NodeBuilder<?, ?, S> child) {
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
    public B runNative(RunConsumer.RC<S> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public B run(RunConsumer.RC0<S> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1> B run(RunConsumer.RC1<S, T1> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2> B run(RunConsumer.RC2<S, T1, T2> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3> B run(RunConsumer.RC3<S, T1, T2, T3> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3, T4> B run(RunConsumer.RC4<S, T1, T2, T3, T4> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, T1, T2, T3, T4, T5> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, T1, T2, T3, T4, T5, T6> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }
    protected abstract B getThis();
}
