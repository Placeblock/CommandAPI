package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parser.ParsedCommand;

@FunctionalInterface
public interface CommandExecutor<S> {

    void run(ParsedCommand<S> parsedCommand, S source);

}
