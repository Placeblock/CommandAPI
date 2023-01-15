package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.CommandExecutor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@RequiredArgsConstructor
public class Command<S extends CommandExecutor<?, ?>> {

    private final String name;
    private final List<Command<S>> children = new ArrayList<>();

    public void command(Command<S> command) {
        this.children.add(command);
    }

}
