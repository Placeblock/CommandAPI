package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;

/**
 * Author: Placeblock
 */
public class DoubleParameter<S> extends NumberParameter<S, Double> {
    private static final char[] POSSIBLE_CHARS = "0123456789.".toCharArray();

    public DoubleParameter(Double min, Double max) {
        super(min, max);
    }

    public static <S> DoubleParameter<S> doubleParam(Double min, Double max) {
        return new DoubleParameter<>(min, max);
    }

    public static <S> DoubleParameter<S> doubleParam(Double max) {
        return new DoubleParameter<>(0D, max);
    }

    @Override
    public Double parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        Double result = command.getReader().readDouble();
        return this.checkNumber(result);
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        calculateSuggestions(suggestionBuilder, partial, this.min, this.max);
    }

    public static <S> void calculateSuggestions(SuggestionBuilder<S> suggestionBuilder, String partial, double min, double max) {
        int partialLength = partial.length();
        if (partialLength == 0) {
            suggestionBuilder.withSuggestion(".");
            if (min <= 0) {
                suggestionBuilder.withSuggestion("-");
            }
        }
        for (char possibleChar : POSSIBLE_CHARS) {
            try {
                String newDoubleString = partial + possibleChar;
                double newDouble = Double.parseDouble(newDoubleString);
                if (((newDouble >= 0 && newDouble <= max) || (newDouble < 0 && newDouble >= min))
                    && (possibleChar != '.' || ( newDouble >=0 && newDouble+1>min ) || (newDouble<0 && newDouble-1<max ))) {
                    suggestionBuilder.withSuggestion(newDoubleString);
                }
            } catch (NumberFormatException ignored) {}
        }
    }

}
