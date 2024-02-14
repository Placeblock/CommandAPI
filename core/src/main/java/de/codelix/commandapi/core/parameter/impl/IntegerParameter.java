package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.exceptions.IntegerTooSmallParseException;
import de.codelix.commandapi.core.parameter.exceptions.InvalidIntegerParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.parser.Source;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class IntegerParameter<S extends Source<M>, M> implements Parameter<Integer, S, M> {
    private final int min;
    private final int max;
    @Override
    public Integer parse(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        assert next != null;
        try {
            int parsed = Integer.parseInt(next);
            if (parsed < this.min) {
                throw new IntegerTooSmallParseException(parsed, this.min);
            }
            if (parsed > this.max) {
                throw new IntegerTooSmallParseException(parsed, this.max);
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new InvalidIntegerParseException(next);
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S, M> ctx, ParsedCommand<S, M> cmd) {
        String next = ctx.getRemaining();
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String suggestion = (next != null ? next : "") + i;
            try {
                // Suggest only lower or equals than maximum
                if (Integer.parseInt(suggestion) > this.max) continue;
                suggestions.add(suggestion);
            } catch (NumberFormatException ignored) {}
        }
        return suggestions;
    }
}
