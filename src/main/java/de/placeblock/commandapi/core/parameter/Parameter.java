package de.placeblock.commandapi.core.parameter;

/**
 * Author: Placeblock
 */
public interface Parameter<T> {

    T parse();

    T getSuggestions();

}
