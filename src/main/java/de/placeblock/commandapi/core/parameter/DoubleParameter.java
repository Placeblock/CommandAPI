package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
public class DoubleParameter<S> extends NumberParameter<S, Double> {
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
    public Double parse(ParsedCommand<S> command) throws CommandSyntaxException {
        Double result = command.getReader().readDouble();
        return this.checkNumber(result);
    }

    @Override
    public List<String> getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        List<String> suggestions = new ArrayList<>();
        String partial = suggestionBuilder.getRemaining();
        // Suggest nothing if higher than maximum
        // Suggest nothing if partial is < 0 and partial is smaller than min
        boolean partialHasDot = partial.contains(".");
        if (!partialHasDot) {
            suggestions.add(".");
        }
        if (partial.split("\\.", -1).length > 2) return new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String suggestion = partial + i;
            try {
                double newDouble = Double.parseDouble(suggestion);
                if (newDouble > this.max) continue;
                if (partialHasDot && newDouble < this.min) continue;
                suggestions.add(suggestion);
            } catch (NumberFormatException ignored) {}
        }
        return suggestions;
    }
}
