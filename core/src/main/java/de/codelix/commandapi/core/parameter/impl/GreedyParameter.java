package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

@SuppressWarnings("unused")
public class GreedyParameter<S> implements Parameter<String, S> {
    @Override
    public String parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException {
        return ctx.getRemaining();
    }
}
