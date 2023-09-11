package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parser.ParameterHolder;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SuggestionBuilder<S> extends ParameterHolder {

    private final TreeCommand<S> treeCommand;
    private final String remaining;
    private final S source;
    private final List<String> suggestions = new ArrayList<>();

    public SuggestionBuilder(TreeCommand<S> treeCommand, String remaining, S source, Map<ParameterTreeCommand<?, ?>, Object> parsedParameters) {
        super(parsedParameters);
        this.treeCommand = treeCommand;
        this.remaining = remaining;
        this.source = source;
    }

    @SuppressWarnings("UnusedReturnValue")
    public SuggestionBuilder<S> withSuggestion(String suggestion) {
        this.suggestions.add(suggestion);
        return this;
    }

    public void withSuggestions(List<String> suggestions) {
        this.suggestions.addAll(suggestions);
    }

    public void withSuggestions(String[] suggestions) {
        this.suggestions.addAll(List.of(suggestions));
    }
}
