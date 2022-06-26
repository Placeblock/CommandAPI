package de.placeblock.commandapi.core.context;

import de.placeblock.commandapi.core.util.StringReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ParseResults<S> {
    private final CommandContextBuilder<S> context;
    private final StringReader reader;
}
