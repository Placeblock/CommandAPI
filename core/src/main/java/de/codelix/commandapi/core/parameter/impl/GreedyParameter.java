package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;

@SuppressWarnings("unused")
public class GreedyParameter<S extends Source<M>, M> implements Parameter<String, S, M> {
    @Override
    public String parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) throws ParseException {
        return ctx.getRemaining();
    }
}
