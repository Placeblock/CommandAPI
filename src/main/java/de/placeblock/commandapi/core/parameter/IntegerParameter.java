package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import io.schark.design.texts.Texts;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@RequiredArgsConstructor
public class IntegerParameter<S> implements Parameter<S, Integer> {
    private final int min;
    private final int max;

    public static <S> IntegerParameter<S> integer(int min, int max) {
        return new IntegerParameter<>(min, max);
    }

    public static <S> IntegerParameter<S> integer(int max) {
        return new IntegerParameter<>(0, max);
    }

    @Override
    public Integer parse(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) throws CommandException {
        int result = context.getReader().readInt();
        if (result < this.min) {
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+result+" <color:inferior>ist <color:negative>zu klein<color:inferior>. Das Minimum ist <color:negative>" + this.min));
        }
        if (result > this.max) {
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+result+" <color:inferior>ist <color:negative>zu groß<color:inferior>. Das Maximum ist <color:negative>" + this.max));
        }
        return result;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) {
        List<String> suggestions = new ArrayList<>();
        Integer parsedParameter = command != null ? context.getParameter(command.getName(), Integer.class) : null;
        // Suggest nothing if higher than maximum
        if (parsedParameter != null && parsedParameter >= this.max) {
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
