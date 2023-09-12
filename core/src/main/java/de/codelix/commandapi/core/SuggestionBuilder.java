package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.ParameterHolder;
import de.codelix.commandapi.core.tree.CommandNode;
import de.codelix.commandapi.core.tree.ParameterCommandNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SuggestionBuilder<S> extends ParameterHolder {

    private final CommandNode<S> commandNode;
    private final String remaining;
    private final S source;
    private final List<String> suggestions = new ArrayList<>();

    public SuggestionBuilder(CommandNode<S> commandNode, String remaining, S source, Map<ParameterCommandNode<?, ?>, Object> parsedParameters) {
        super(parsedParameters);
        this.commandNode = commandNode;
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
