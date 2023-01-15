package de.placeblock.commandapi.core.tree;

import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
public class LiteralTreeCommand<S> extends TreeCommand<S> {

    public LiteralTreeCommand(String name, List<TreeCommand<S>> children, TextComponent description,
                              List<String> permissions, Consumer<S> run) {
        super(name, children, description, permissions, run);
    }

}
