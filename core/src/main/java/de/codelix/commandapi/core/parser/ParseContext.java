package de.codelix.commandapi.core.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@AllArgsConstructor
public class ParseContext<S> {

    @Setter
    private Queue<String> input;

    private final S source;

    public ParseContext<S> copy() {
        return new ParseContext<>(new LinkedList<>(this.input), this.source);
    }

}
