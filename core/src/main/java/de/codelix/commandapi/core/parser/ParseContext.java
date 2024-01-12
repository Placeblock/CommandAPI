package de.codelix.commandapi.core.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@RequiredArgsConstructor
public class ParseContext<S> {

    private final Queue<String> input;

    private final S source;

    public ParseContext<S> copy() {
        return new ParseContext<>(new LinkedList<>(this.input), this.source);
    }

}
