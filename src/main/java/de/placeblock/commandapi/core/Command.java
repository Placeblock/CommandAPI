package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.context.CommandContext;

@FunctionalInterface
public interface Command<S> {
    void run(CommandContext<S> context);
}
