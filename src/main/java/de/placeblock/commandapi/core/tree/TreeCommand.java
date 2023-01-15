package de.placeblock.commandapi.core.tree;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class TreeCommand<S> {

    private final String name;
    private final List<TreeCommand<S>> children;
    private final TextComponent description;
    private final List<String> permissions;
    private final Consumer<S> run;

    public boolean parse() {

    }

}
