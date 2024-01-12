package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

public class WordParameter<S> implements Parameter<String, S> {
    @Override
    public String parse(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        return ctx.getInput().poll();
    }
}
