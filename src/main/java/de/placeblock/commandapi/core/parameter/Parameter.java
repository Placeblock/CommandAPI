package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;

import java.util.List;

/**
 * Author: Placeblock
 */
public interface Parameter<S, T> {

    T parse(ParsedCommand<S> command) throws CommandSyntaxException;

    List<String> getSuggestions(SuggestionBuilder<S> suggestionBuilder);

    @SuppressWarnings("unused")
    static List<String> startsWith(List<String> list, String partial) {
        return list.stream().filter(item -> item.startsWith(partial)).toList();
    }

}
