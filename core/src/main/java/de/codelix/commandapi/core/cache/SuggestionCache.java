package de.codelix.commandapi.core.cache;

import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuggestionCache<S extends Source<M>, M> {

    private Map<String, CacheEntry<S, M>> entries;

    public void invalidate(Node<S, M> node) {
        this.entries = this.entries.entrySet().stream()
            .filter(e -> !e.getValue().getNode().equals(node))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void invalidate(String input) {
        this.entries.remove(input);
    }

    public List<String> getCached(String input) {
        CacheEntry<S, M> cacheEntry = this.entries.get(input);
        if (cacheEntry == null) return null;
        long timestamp = cacheEntry.getTimestamp();
        Node<S, M> node = cacheEntry.getNode();
        if (timestamp < System.currentTimeMillis() - node.getCacheTime()) {
            this.invalidate(input);
            return null;
        }
        return cacheEntry.getSuggestions();
    }

}
