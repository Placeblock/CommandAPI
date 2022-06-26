package de.placeblock.commandapi.core.context;

import de.placeblock.commandapi.core.util.StringRange;
import lombok.Getter;

@Getter
public class ParsedArgument<S, T> {

    private final StringRange range;
    private final T result;

    public ParsedArgument(int start, int end, T result) {
        this.range = StringRange.between(start, end);
        this.result = result;
    }
}
