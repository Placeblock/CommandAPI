package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.ParsedCommand;

public interface RunConsumer<S> {
    interface RC<S> extends RunConsumer<S> {
        void run(S source, ParsedCommand<S> cmd);
    }
    interface RC0<S> extends RunConsumer<S> {
        void run(S source);
    }
    interface RC1<S, T1> extends RunConsumer<S> {
        void run(S source, T1 arg0);
    }
    interface RC2<S, T1, T2> extends RunConsumer<S> {
        void run(S source, T1 arg0, T2 arg1);
    }
    interface RC3<S, T1, T2, T3> extends RunConsumer<S> {
        void run(S source, T1 arg0, T2 arg1, T3 arg2);
    }
    interface RC4<S, T1, T2, T3, T4> extends RunConsumer<S> {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3);
    }
    interface RC5<S, T1, T2, T3, T4, T5> extends RunConsumer<S> {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4);
    }
    interface RC6<S, T1, T2, T3, T4, T5, T6> extends RunConsumer<S> {
        void run(S source, T1 arg0, T2 arg1, T3 arg2, T4 arg3, T5 arg4, T6 arg5);
    }
}


