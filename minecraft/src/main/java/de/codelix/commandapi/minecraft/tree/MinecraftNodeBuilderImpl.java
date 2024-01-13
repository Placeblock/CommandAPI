package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.Permission;
import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.core.CoreNode;
import de.codelix.commandapi.minecraft.MinecraftSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class MinecraftNodeBuilderImpl<B extends MinecraftNodeBuilderImpl<B, R, S, P, C>, R extends CoreNode<S>, S extends MinecraftSource<P, C>, P, C> implements NodeBuilder<B, R, S> {
    protected String displayName;
    protected List<NodeBuilder<?, ?, S>> children = new ArrayList<>();
    protected Permission permission;
    protected boolean optional = false;
    protected Collection<RunConsumer<S>> runConsumers = new ArrayList<>();

    @Override
    public B displayName(String displayName) {
        this.displayName = displayName;
        return this.getThis();
    }

    @Override
    public B then(NodeBuilder<?, ?, S> child) {
        return this.getThis();
    }

    @Override
    public B permission(Permission permission) {
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

    public B runPlayer(RunConsumer.RC0<S> runConsumer) {
        return this.run(source -> {
            if (source.isPlayer()) {
                runConsumer.run(source);
            }
        });
    }

    @Override
    public <T1> B run(RunConsumer.RC1<S, T1> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1> B runPlayer(RunConsumer.RC1<S, T1> runConsumer) {
        return this.run((S source, T1 arg0) -> {
            if (source.isPlayer()) {
                runConsumer.run(source, arg0);
            }
        });
    }

    @Override
    public <T1, T2> B run(RunConsumer.RC2<S, T1, T2> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2> B runPlayer(RunConsumer.RC2<S, T1, T2> runConsumer) {
        return this.run((S source, T1 arg0, T2 arg1) -> {
            if (source.isPlayer()) {
                runConsumer.run(source, arg0, arg1);
            }
        });
    }

    @Override
    public <T1, T2, T3> B run(RunConsumer.RC3<S, T1, T2, T3> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3> B runPlayer(RunConsumer.RC3<S, T1, T2, T3> runConsumer) {
        return this.run((S source, T1 arg0, T2 arg1, T3 arg2) -> {
            if (source.isPlayer()) {
                runConsumer.run(source, arg0, arg1, arg2);
            }
        });
    }

    @Override
    public <T1, T2, T3, T4> B run(RunConsumer.RC4<S, T1, T2, T3, T4> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4> B runPlayer(RunConsumer.RC4<S, T1, T2, T3, T4> runConsumer) {
        return this.run((S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3) -> {
            if (source.isPlayer()) {
                runConsumer.run(source, arg0, arg1, arg2, arg3);
            }
        });
    }

    @Override
    public <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, T1, T2, T3, T4, T5> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4, T5> B runPlayer(RunConsumer.RC5<S, T1, T2, T3, T4, T5> runConsumer) {
        return this.run((S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4) -> {
            if (source.isPlayer()) {
                runConsumer.run(source, arg0, arg1, arg2, arg3, arg4);
            }
        });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, T1, T2, T3, T4, T5, T6> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4, T5, T6> B runPlayer(RunConsumer.RC6<S, T1, T2, T3, T4, T5, T6> runConsumer) {
        return this.run((S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5) -> {
            if (source.isPlayer()) {
                runConsumer.run(source, arg0, arg1, arg2, arg3, arg4, arg5);
            }
        });
    }

    protected abstract B getThis();
}
