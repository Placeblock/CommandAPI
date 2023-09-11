package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;

/**
 * Author: Placeblock
 */
public class IntegerParameter<S> extends NumberParameter<S, Integer> {
    public IntegerParameter(int min, int max) {
        super(min, max);
    }

    public static <S> IntegerParameter<S> integer(int min, int max) {
        return new IntegerParameter<>(min, max);
    }

    public static <S> IntegerParameter<S> integer(int max) {
        return new IntegerParameter<>(0, max);
    }

    @Override
    public Integer parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        Integer result = command.getReader().readInt();
        return this.checkNumber(result);
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        for (int i = 0; i < 10; i++) {
            String suggestion = (partial != null ? partial : "") + i;
            try {
                // Suggest only lower or equals than maximum
                if (Integer.parseInt(suggestion) > this.max) continue;
                suggestionBuilder.withSuggestion(suggestion);
            } catch (NumberFormatException ignored) {}
        }
    }

}
