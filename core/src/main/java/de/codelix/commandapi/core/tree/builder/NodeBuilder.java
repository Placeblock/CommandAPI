package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
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

    public <S> B runNative(RunConsumer.RC<S> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S> B run(RunConsumer.RC0<S> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1> B run(RunConsumer.RC1<S, T1> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2> B run(RunConsumer.RC2<S, T1, T2> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3> B run(RunConsumer.RC3<S, T1, T2, T3> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4> B run(RunConsumer.RC4<S, T1, T2, T3, T4> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, T1, T2, T3, T4, T5> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, T1, T2, T3, T4, T5, T6> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4, T5, T6, T7> B run(RunConsumer.RC7<S, T1, T2, T3, T4, T5, T6, T7> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4, T5, T6, T7, T8> B run(RunConsumer.RC8<S, T1, T2, T3, T4, T5, T6, T7, T8> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4, T5, T6, T7, T8, T9> B run(RunConsumer.RC9<S, T1, T2, T3, T4, T5, T6, T7, T8, T9> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> B run(RunConsumer.RC10<S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> runConsumer) {
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
