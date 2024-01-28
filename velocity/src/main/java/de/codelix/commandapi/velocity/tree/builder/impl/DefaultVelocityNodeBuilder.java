package de.codelix.commandapi.velocity.tree.builder.impl;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.minecraft.tree.builder.PlayerRunConsumer;
import de.codelix.commandapi.velocity.VelocitySource;
import de.codelix.commandapi.velocity.tree.builder.VelocityNodeBuilder;
import de.codelix.commandapi.velocity.tree.impl.DefaultVelocityNode;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class DefaultVelocityNodeBuilder<B extends DefaultVelocityNodeBuilder<B, R, S, P>, R extends DefaultVelocityNode<S, P>, S extends VelocitySource<P>, P> implements VelocityNodeBuilder<B, R, S, P> {
    protected String displayName;
    protected String description;
    protected List<NodeBuilder<?, ?, S, TextComponent>> children = new ArrayList<>();
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
    public B then(NodeBuilder<?, ?, S, TextComponent> child) {
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
    public B runNative(RunConsumer.RC<S, TextComponent> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    @Override
    public B run(RunConsumer.RC0<S, TextComponent> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public B runPlayer(PlayerRunConsumer.RC0<P> runConsumer) {
        return this.getThis().run(source -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer());
            }
        });
    }

    @Override
    public <T1> B run(RunConsumer.RC1<S, TextComponent, T1> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1> B runPlayer(PlayerRunConsumer.RC1<P, T1> runConsumer) {
        return this.getThis().run((S source, T1 arg0) -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer(), arg0);
            }
        });
    }

    @Override
    public <T1, T2> B run(RunConsumer.RC2<S, TextComponent, T1, T2> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2> B runPlayer(PlayerRunConsumer.RC2<P, T1, T2> runConsumer) {
        return this.getThis().run((S source, T1 arg0, T2 arg1) -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer(), arg0, arg1);
            }
        });
    }

    @Override
    public <T1, T2, T3> B run(RunConsumer.RC3<S, TextComponent, T1, T2, T3> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3> B runPlayer(PlayerRunConsumer.RC3<P, T1, T2, T3> runConsumer) {
        return this.getThis().run((S source, T1 arg0, T2 arg1, T3 arg2) -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer(), arg0, arg1, arg2);
            }
        });
    }

    @Override
    public <T1, T2, T3, T4> B run(RunConsumer.RC4<S, TextComponent, T1, T2, T3, T4> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4> B runPlayer(PlayerRunConsumer.RC4<P, T1, T2, T3, T4> runConsumer) {
        return this.getThis().run((S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3) -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer(), arg0, arg1, arg2, arg3);
            }
        });
    }

    @Override
    public <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, TextComponent, T1, T2, T3, T4, T5> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4, T5> B runPlayer(PlayerRunConsumer.RC5<P, T1, T2, T3, T4, T5> runConsumer) {
        return this.getThis().run((S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4) -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer(), arg0, arg1, arg2, arg3, arg4);
            }
        });
    }

    @Override
    public <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, TextComponent, T1, T2, T3, T4, T5, T6> runConsumer) {
        this.runConsumers.add(runConsumer);
        return this.getThis();
    }

    public <T1, T2, T3, T4, T5, T6> B runPlayer(PlayerRunConsumer.RC6<P, T1, T2, T3, T4, T5, T6> runConsumer) {
        return this.getThis().run((S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5) -> {
            if (source.isPlayer()) {
                runConsumer.run(source.getPlayer(), arg0, arg1, arg2, arg3, arg4, arg5);
            }
        });
    }

    protected abstract B getThis();
}
