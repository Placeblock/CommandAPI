package de.codelix.commandapi.minecraft.tree.builder;

import de.codelix.commandapi.core.tree.builder.NodeBuilder;
import de.codelix.commandapi.minecraft.MinecraftSource;
import de.codelix.commandapi.minecraft.tree.MinecraftNode;

@SuppressWarnings("unused")
public interface MinecraftNodeBuilder<B extends MinecraftNodeBuilder<B, R, S, P, C, M>, R extends MinecraftNode<S, P, C, M>, S extends MinecraftSource<P, C, M>, P, C, M> extends NodeBuilder<B, R, S, M> {

    B runPlayer(PlayerRunConsumer.RC0<P> runConsumer);

    <T1> B runPlayer(PlayerRunConsumer.RC1<P, T1> runConsumer);

    <T1, T2> B runPlayer(PlayerRunConsumer.RC2<P, T1, T2> runConsumer);

    <T1, T2, T3> B runPlayer(PlayerRunConsumer.RC3<P, T1, T2, T3> runConsumer);

    <T1, T2, T3, T4> B runPlayer(PlayerRunConsumer.RC4<P, T1, T2, T3, T4> runConsumer);

    <T1, T2, T3, T4, T5> B runPlayer(PlayerRunConsumer.RC5<P,  T1, T2, T3, T4, T5> runConsumer);

    <T1, T2, T3, T4, T5, T6> B runPlayer(PlayerRunConsumer.RC6<P, T1, T2, T3, T4, T5, T6> runConsumer);
}
