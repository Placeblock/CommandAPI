package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.exceptions.DoubleTooLargeParseException;
import de.codelix.commandapi.core.parameter.exceptions.DoubleTooSmallParseException;
import de.codelix.commandapi.core.parameter.exceptions.InvalidDoubleParseException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DoubleParameter<S> implements Parameter<Double, S> {
    private static final char[] POSSIBLE_CHARS = "0123456789.".toCharArray();
    private final double min;
    private final double max;

    @Override
    public Double parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        assert next != null;
        try {
            double parsed = Double.parseDouble(next);
            if (parsed < this.min) {
                throw new DoubleTooSmallParseException(parsed, this.min);
            }
            if (parsed > this.max) {
                throw new DoubleTooLargeParseException(parsed, this.max);
            }
            return parsed;
        } catch (NumberFormatException ex) {
            throw new InvalidDoubleParseException(next);
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        String next = ctx.getRemaining();
        List<String> suggestions = new ArrayList<>();
        int partialLength = next.length();
        if (partialLength == 0) {
            suggestions.add(".");
            if (this.min <= 0) {
                suggestions.add("-");
            }
        }
        for (char possibleChar : POSSIBLE_CHARS) {
            try {
                String newDoubleString = next + possibleChar;
                double newDouble = Double.parseDouble(newDoubleString);
                if (((newDouble >= 0 && newDouble <= this.max) || (newDouble < 0 && newDouble >= this.min))
                    && (possibleChar != '.' || ( newDouble >=0 && newDouble+1>this.min ) || (newDouble<0 && newDouble-1<this.max ))) {
                    suggestions.add(newDoubleString);
                }
            } catch (NumberFormatException ignored) {}
        }
        return suggestions;
    }
}
