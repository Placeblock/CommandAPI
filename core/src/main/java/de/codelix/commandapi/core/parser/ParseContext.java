package de.codelix.commandapi.core.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@AllArgsConstructor
public class ParseContext<S extends Source<M>, M> {

    @Setter
    private Queue<String> input;

    private final S source;

    public ParseContext<S, M> copy() {
        return new ParseContext<>(new LinkedList<>(this.input), this.source);
    }

    public String getRemaining() {
        return String.join(" ", this.input);
    }

    public boolean hasPermission(String permission) {
        return permission == null || this.source.hasPermission(permission);
    }
}
