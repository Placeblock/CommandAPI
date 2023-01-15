package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;

import java.util.List;

/**
 * Author: Placeblock
 */
public interface Parameter<S, T> {

    T parse(ParseContext<S> context);

    List<String> getSuggestions(ParseContext<S> context);

}
