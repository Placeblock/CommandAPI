package de.codelix.commandapi.minecraft.tree;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.core.tree.core.CoreNode;
import de.codelix.commandapi.minecraft.MinecraftSource;

public interface MinecraftNodeBuilder<B extends MinecraftNodeBuilder<B, R, S, P, C>, R extends CoreNode<S>, S extends MinecraftSource<P, C>, P, C> extends NodeBuilder<B, R, S> {
    B runPlayer(RunConsumer.RC0<P> runConsumer);

    <T1> B runPlayer(RunConsumer.RC1<P, T1> runConsumer);

    <T1, T2> B runPlayer(RunConsumer.RC2<P, T1, T2> runConsumer);

    <T1, T2, T3> B runPlayer(RunConsumer.RC3<P, T1, T2, T3> runConsumer);

    <T1, T2, T3, T4> B runPlayer(RunConsumer.RC4<P, T1, T2, T3, T4> runConsumer);

    <T1, T2, T3, T4, T5> B runPlayer(RunConsumer.RC5<P, T1, T2, T3, T4, T5> runConsumer);

    <T1, T2, T3, T4, T5, T6> B runPlayer(RunConsumer.RC6<P, T1, T2, T3, T4, T5, T6> runConsumer);
}
