package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedParameter;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    public ParsedParameter<?> parse(ParseContext<S> context, ParameterTreeCommand<S, Double> command) throws CommandException {
        double result = context.getReader().readDouble();
        this.checkNumber(result);
        return result;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, @Nullable ParameterTreeCommand<S, Double> command) {
        List<String> suggestions = new ArrayList<>();
        Double parsedParameter = command != null ? context.getParameter(command.getName(), Double.class) : null;
        String partial = context.getReader().readUnquotedString();
        System.out.println(partial);
        // Suggest nothing if higher than maximum
        // Suggest nothing if parsedParameter is < 0 and parsedParameter is smaller than min
        if (parsedParameter != null && (parsedParameter >= this.max || (parsedParameter < 0 && parsedParameter < this.min))) {
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
