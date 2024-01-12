package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.parameter.Parameter;

/**
 * An argument is a node that can parse any value. How and if it is parsed is specified by the parameter
 * @param <T>
 */
public interface Argument<T> extends Node {

    /**
     * The name of the argument to access it later
     * @return The name
     */
    String getName();

    /**
     * The parameter that parses the argument
     * @return The parameter
     */
    Parameter<T> getParameter();

}
