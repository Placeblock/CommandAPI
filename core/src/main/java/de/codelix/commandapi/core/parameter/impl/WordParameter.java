package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

public class WordParameter implements Parameter<String> {
    @Override
    public String parse(ParseContext ctx, ParsedCommand cmd) {
        return ctx.getInput().poll();
    }
}
