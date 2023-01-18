package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;

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
    public ParsedValue<Integer> parse(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) {
        ParsedValue<Integer> result = context.getReader().readInt();
        return this.checkNumber(result);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) {
        List<String> suggestions = new ArrayList<>();
        ParsedValue<Integer> parsedParameter = command != null ? context.getParameter(command.getName(), Integer.class) : null;
        assert parsedParameter != null;
        Integer parsedValue = parsedParameter.getParsed();
        // Suggest nothing if higher than maximum
        if ((parsedValue != null && parsedValue >= this.max) || (parsedParameter.hasException() && context.isNotParsedToEnd())) {
            return new ArrayList<>();
        }
        // Suggest only lower or equals than maximum
        for (int i = 0; i < 10; i++) {
            String suggestion = (parsedValue != null ? parsedValue.toString() : "") + i;
            if (Integer.parseInt(suggestion) > this.max) continue;
            suggestions.add(suggestion);
        }
        return suggestions;
    }

}
