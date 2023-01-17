package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedParameter;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: Placeblock
 */
public interface Parameter<S, T> {

    ParsedParameter parse(ParseContext<S> context, ParameterTreeCommand<S, T> command) throws CommandException;

    List<String> getSuggestions(ParseContext<S> context, @Nullable ParameterTreeCommand<S, T> command);

    default List<String> startsWith(List<String> list, String partial) {
        return list.stream().filter(item -> item.startsWith(partial)).toList();
    }

}
