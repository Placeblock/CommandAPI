package de.placeblock.commandapi.context;

import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.util.StringReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ParseResults<S> {
    private final CommandContextBuilder<S> context;
    private final StringReader reader;
    private final Map<CommandNode<S>, CommandSyntaxException> exceptions;
}
