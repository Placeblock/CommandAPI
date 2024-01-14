package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class SetParameter<S> implements Parameter<String, S> {
    private final Set<String> values;
    @Override
    public String parse(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        String next = ctx.getInput().poll();
        for (String value : this.values) {
            if (value.equals(next)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        String next = ctx.getRemaining();
        return this.startsWith(this.values, next);
    }
}
