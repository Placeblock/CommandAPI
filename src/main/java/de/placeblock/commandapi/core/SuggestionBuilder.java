package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SuggestionBuilder<S> {

    private final TreeCommand<S> treeCommand;
    private final String remaining;
    private final S source;
    private final List<String> suggestions = new ArrayList<>();

    public void withSuggestion(String suggestion) {
        this.suggestions.add(suggestion);
    }

    public void withSuggestions(List<String> suggestions) {
        this.suggestions.addAll(suggestions);
    }

    public void withSuggestions(String[] suggestions) {
        this.suggestions.addAll(List.of(suggestions));
    }
}
