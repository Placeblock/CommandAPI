package de.codelix.commandapi.core.tree;

import java.util.List;

/**
 * A literal is a static node with one or more values
 */
public interface Literal<S> extends Node<S> {

    /**
     * All names that match a literal
     * @return the names
     */
    List<String> getNames();
}
