package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.ParsedCommand;

public interface RunConsumer {
    interface RC<S> extends RunConsumer {
        void run(S source, ParsedCommand cmd);
    }
    interface RC0<S> extends RunConsumer {
        void run(S source);
    }
    interface RC1<S, T1> extends RunConsumer {
        void run(S source, T1 arg0);
    }
    interface RC2<S, T1, T2> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1);
    }
    interface RC3<S, T1, T2, T3> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2);
    }
    interface RC4<S, T1, T2, T3, T4> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3);
    }
    interface RC5<S, T1, T2, T3, T4, T5> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4);
    }
    interface RC6<S, T1, T2, T3, T4, T5, T6> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5);
    }
    interface RC7<S, T1, T2, T3, T4, T5, T6, T7> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5, T7 arg6);
    }
    interface RC8<S, T1, T2, T3, T4, T5, T6, T7, T8> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5, T7 arg6, T8 arg7);
    }
    interface RC9<S, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5, T7 arg6, T8 arg7, T9 arg8);
    }
    interface RC10<S, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends RunConsumer {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5, T7 arg6, T8 arg7, T9 arg8, T10 arg9);
    }
}


