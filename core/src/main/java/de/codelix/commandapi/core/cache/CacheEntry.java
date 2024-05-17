package de.codelix.commandapi.core.cache;

import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CacheEntry<S extends Source<M>, M> {

    private final String input;
    private final List<String> suggestions;
    private final Node<S, M> node;
    private final long timestamp;

}
