package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;

/**
 * Author: Placeblock
 */
public class DoubleParameter<S> extends NumberParameter<S, Double> {
    private final char[] possibleChars = "0123456789.".toCharArray();

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
        int partialLength = partial.length();
        if (partialLength == 0) {
            suggestionBuilder.withSuggestion(".");
            if (this.min <= 0) {
                suggestionBuilder.withSuggestion("-");
            }
        }
        for (char possibleChar : this.possibleChars) {
            try {
                String newDoubleString = partial + possibleChar;
                double newDouble = Double.parseDouble(newDoubleString);
                if (((newDouble >= 0 && newDouble <= this.max) || (newDouble < 0 && newDouble >= this.min))
                    && (possibleChar != '.' || ( newDouble >=0 && newDouble+1>this.min ) || (newDouble<0 && newDouble-1<this.max ))) {
                    suggestionBuilder.withSuggestion(newDoubleString);
                }
            } catch (NumberFormatException ignored) {}
        }
    }

}
