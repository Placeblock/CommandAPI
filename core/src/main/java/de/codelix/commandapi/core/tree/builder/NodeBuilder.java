package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.tree.Node;

@SuppressWarnings("unused")
public interface NodeBuilder<B extends NodeBuilder<B, R, S>, R extends Node<S>, S> {

    B displayName(String displayName);

    @SuppressWarnings("UnusedReturnValue")
    B then(NodeBuilder<?, ?, S> child);

    B permission(String permission);

    B optional();

    B unsafePermission();

    B runNative(RunConsumer.RC<S> runConsumer);

    B run(RunConsumer.RC0<S> runConsumer);

    <T1> B run(RunConsumer.RC1<S, T1> runConsumer);

    <T1, T2> B run(RunConsumer.RC2<S, T1, T2> runConsumer);

    <T1, T2, T3> B run(RunConsumer.RC3<S, T1, T2, T3> runConsumer);

    <T1, T2, T3, T4> B run(RunConsumer.RC4<S, T1, T2, T3, T4> runConsumer);

    <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, T1, T2, T3, T4, T5> runConsumer);

    <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, T1, T2, T3, T4, T5, T6> runConsumer);


    R build();
}
