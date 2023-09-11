package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;

import java.util.List;

/**
 * Author: Placeblock
 */
public interface Parameter<S, T> {

    T parse(ParsedCommandBranch<S> command, S source) throws CommandParseException;

    void getSuggestions(SuggestionBuilder<S> suggestionBuilder);

    @SuppressWarnings("unused")
    static List<String> startsWith(List<String> list, String partial) {
        return list.stream().filter(item -> item.startsWith(partial)).toList();
    }

}
