package de.codelix.commandapi.minecraft.tree.builder;

import de.codelix.commandapi.core.RunConsumer;

public interface PlayerRunConsumer {
    interface RC0<P> extends RunConsumer {
        void run(P player);
    }
    interface RC1<P, T1> extends RunConsumer {
        void run(P player, T1 arg0);
    }
    interface RC2<P, T1, T2> extends RunConsumer {
        void run(P player, T1 arg0, T2 arg1);
    }
    interface RC3<P, T1, T2, T3> extends RunConsumer {
        void run(P player, T1 arg0, T2 arg1, T3 arg2);
    }
    interface RC4<P, T1, T2, T3, T4> extends RunConsumer {
        void run(P player, T1 arg0, T2 arg1, T3 arg2, T4 arg3);
    }
    interface RC5<P, T1, T2, T3, T4, T5> extends RunConsumer {
        void run(P player, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4);
    }
    interface RC6<P, T1, T2, T3, T4, T5, T6> extends RunConsumer {
        void run(P player, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5);
    }
}
