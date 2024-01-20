package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class SetParameter<S extends Source<M>, M> implements Parameter<String, S, M> {
    private final Set<String> values;
    @Override
    public String parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        String next = ctx.getInput().poll();
        for (String value : this.values) {
            if (value.equals(next)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        String next = ctx.getRemaining();
        return this.startsWith(this.values, next);
    }
}
