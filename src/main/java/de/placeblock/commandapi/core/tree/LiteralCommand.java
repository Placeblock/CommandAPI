package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.CommandExecutor;

/**
 * Author: Placeblock
 */
public class LiteralCommand<S extends CommandExecutor> extends Command<S> {

    public LiteralCommand(String name) {
        super(name);
    }

}
