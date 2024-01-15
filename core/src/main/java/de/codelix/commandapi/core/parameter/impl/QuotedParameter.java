package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.exceptions.MarkMissingParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;

import java.util.ArrayList;
import java.util.List;

public class QuotedParameter<S> implements Parameter<String, S> {
    public static final List<Character> MARK = List.of('"', '\'');
    @Override
    public String parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        assert next != null;
        if (isInvalidMark(next.charAt(0))) {
            throw new MarkMissingParseException(this, next);
        }
        StringBuilder parsed = new StringBuilder(next);
        while (next != null && isInvalidMark(next.charAt(next.length() - 1))) {
            next = ctx.getInput().poll();
            parsed.append(next);
        }
        if (next != null) {
            return parsed.toString();
        } else {
            throw new MarkMissingParseException(this, parsed.toString());
        }
    }

    private static boolean isInvalidMark(Character c) {
        return !MARK.contains(c);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        String remaining = ctx.getRemaining();
        if (!remaining.endsWith("\"")) {
            return List.of(remaining+"\"");
        }
        return new ArrayList<>();
    }
}
