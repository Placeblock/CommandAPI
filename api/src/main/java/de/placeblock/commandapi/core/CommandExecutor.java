package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parser.ParsedCommandBranch;

@FunctionalInterface
public interface CommandExecutor<S> {

    void run(ParsedCommandBranch<S> parsedCommandBranch, S source);

}
