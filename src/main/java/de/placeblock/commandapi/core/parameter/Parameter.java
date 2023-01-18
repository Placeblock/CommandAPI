package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;

import java.util.List;

/**
 * Author: Placeblock
 */
public interface Parameter<S, T> {

    ParsedValue<T> parse(ParseContext<S> context, ParameterTreeCommand<S, T> command);

    List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, T> command);

    @SuppressWarnings("unused")
    default List<String> startsWith(List<String> list, String partial) {
        return list.stream().filter(item -> item.startsWith(partial)).toList();
    }

}
