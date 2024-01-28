package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.RunConsumer;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;

@SuppressWarnings("unused")
public interface NodeBuilder<B extends NodeBuilder<B, R, S, M>, R extends Node<S, M>, S extends Source<M>, M> {

    B displayName(String displayName);

    B description(String description);

    @SuppressWarnings("UnusedReturnValue")
    B then(NodeBuilder<?, ?, S, M> child);

    B permission(String permission);

    B optional();

    B unsafePermission();

    B runNative(RunConsumer.RC<S, M> runConsumer);

    B run(RunConsumer.RC0<S, M> runConsumer);

    <T1> B run(RunConsumer.RC1<S, M, T1> runConsumer);

    <T1, T2> B run(RunConsumer.RC2<S, M, T1, T2> runConsumer);

    <T1, T2, T3> B run(RunConsumer.RC3<S, M, T1, T2, T3> runConsumer);

    <T1, T2, T3, T4> B run(RunConsumer.RC4<S, M, T1, T2, T3, T4> runConsumer);

    <T1, T2, T3, T4, T5> B run(RunConsumer.RC5<S, M, T1, T2, T3, T4, T5> runConsumer);

    <T1, T2, T3, T4, T5, T6> B run(RunConsumer.RC6<S, M, T1, T2, T3, T4, T5, T6> runConsumer);


    R build();
}
