package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.parser.Source;

import java.util.List;

/**
 * A literal is a static node with one or more values
 */
public interface Literal<S extends Source<M>, M> extends Node<S, M> {

    /**
     * All names that match a literal
     * @return the names
     */
    List<String> getNames();
}
