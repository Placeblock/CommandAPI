package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedParameter;
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
    public ParsedParameter<?> parse(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) throws CommandException {
        Integer result = context.getReader().readInt();
        this.checkNumber(result);
        return new ParsedParameter<>(result, result.toString());
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) {
        List<String> suggestions = new ArrayList<>();
        Integer parsedParameter = command != null ? context.getParameter(command.getName(), Integer.class) : null;
        // Suggest nothing if higher than maximum
        if ((parsedParameter != null && parsedParameter >= this.max) || (context.hasError(command) && !context.isParsedToEnd())) {
            return new ArrayList<>();
        }
        // Suggest only lower or equals than maximum
        for (int i = 0; i < 10; i++) {
            String suggestion = (parsedParameter != null ? parsedParameter.toString() : "") + i;
            if (Integer.parseInt(suggestion) > this.max) continue;
            suggestions.add(suggestion);
        }
        return suggestions;
    }

}
