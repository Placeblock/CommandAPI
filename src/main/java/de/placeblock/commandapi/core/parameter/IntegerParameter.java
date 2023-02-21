package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;

import java.util.ArrayList;
import java.util.List;

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
    public Integer parse(ParsedCommand<S> command) throws CommandSyntaxException {
        Integer result = command.getReader().readInt();
        return this.checkNumber(result);
    }

    @Override
    public List<String> getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        List<String> suggestions = new ArrayList<>();
        String partial = suggestionBuilder.getRemaining();
        for (int i = 0; i < 10; i++) {
            String suggestion = (partial != null ? partial : "") + i;
            try {
                // Suggest only lower or equals than maximum
                if (Integer.parseInt(suggestion) > this.max) continue;
                suggestions.add(suggestion);
            } catch (NumberFormatException ignored) {}
        }
        return suggestions;
    }

}
