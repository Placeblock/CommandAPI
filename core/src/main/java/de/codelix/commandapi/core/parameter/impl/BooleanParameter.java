package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.exceptions.InvalidBooleanParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

import java.util.List;

@SuppressWarnings("unused")
public class BooleanParameter<S> implements Parameter<Boolean, S> {
    @Override
    public Boolean parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        if ("true".equals(next)) return true;
        if ("false".equals(next)) return false;
        throw new InvalidBooleanParseException(this, next);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        String next = ctx.getRemaining();
        return this.startsWith(List.of("true", "end"), next);
    }
}
