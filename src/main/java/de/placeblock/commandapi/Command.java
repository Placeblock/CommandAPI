package de.placeblock.commandapi;

import de.placeblock.commandapi.context.CommandContext;

@FunctionalInterface
public interface Command<S> {
    void run(CommandContext<S> context);
}
