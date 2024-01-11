package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.parameter.Parameter;

public interface Attribute<T> extends Node {

    String getName();

    Parameter<T> getParameter();

}
