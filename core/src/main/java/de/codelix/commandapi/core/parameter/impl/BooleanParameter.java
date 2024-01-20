package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.exceptions.InvalidBooleanParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;

import java.util.List;

@SuppressWarnings("unused")
public class BooleanParameter<S extends Source<M>, M> implements Parameter<Boolean, S, M> {
    @Override
    public Boolean parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        if ("true".equals(next)) return true;
        if ("false".equals(next)) return false;
        throw new InvalidBooleanParseException(this, next);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        String next = ctx.getRemaining();
        return this.startsWith(List.of("true", "end"), next);
    }
}
