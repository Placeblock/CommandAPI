package de.codelix.commandapi.core.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@RequiredArgsConstructor
public class ParseContext {

    private final Queue<String> input;

    public ParseContext copy() {
        return new ParseContext(new LinkedList<>(this.input));
    }

}
