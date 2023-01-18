package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import org.jetbrains.annotations.Nullable;

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
    public ParsedValue<Double> parse(ParseContext<S> context, ParameterTreeCommand<S, Double> command) {
        ParsedValue<Double> result = context.getReader().readDouble();
        return this.checkNumber(result);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, @Nullable ParameterTreeCommand<S, Double> command) {
        List<String> suggestions = new ArrayList<>();
        ParsedValue<Double> parsedParameter = command != null ? context.getParameter(command.getName(), Double.class) : null;
        assert parsedParameter != null;
        Double parsedValue = parsedParameter.getParsed();
        String partial = parsedParameter.getString();
        System.out.println(partial);
        // Suggest nothing if higher than maximum
        // Suggest nothing if parsedParameter is < 0 and parsedParameter is smaller than min
        if (parsedValue != null && (parsedValue >= this.max || (parsedValue < 0 && parsedValue < this.min))) {
            return new ArrayList<>();
        }
        if (!partial.contains(".")) {
            suggestions.add(".");
        }
        if (partial.split("\\.", -1).length > 2) return new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String suggestion = partial + i;
            try {
                if (Double.parseDouble(suggestion) > this.max) continue;
                suggestions.add(suggestion);
            } catch (NumberFormatException ignored) {}
        }
        return suggestions;
    }
}
