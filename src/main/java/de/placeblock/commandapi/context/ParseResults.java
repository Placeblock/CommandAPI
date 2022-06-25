package de.placeblock.commandapi.context;

import de.placeblock.commandapi.util.StringReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParseResults<S> {
    private final CommandContextBuilder<S> context;
    private final StringReader reader;
}
