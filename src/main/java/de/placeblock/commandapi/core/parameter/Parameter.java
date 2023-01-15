package de.placeblock.commandapi.core.parameter;

import java.util.List;

/**
 * Author: Placeblock
 */
public interface Parameter<S, T> {

    T parse();

    List<String> getSuggestions(S );

}
