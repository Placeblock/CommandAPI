package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.ParsedCommandBranch;

@FunctionalInterface
public interface CommandExecutor<S> {

    void run(ParsedCommandBranch<S> parsedCommandBranch, S source);

}
