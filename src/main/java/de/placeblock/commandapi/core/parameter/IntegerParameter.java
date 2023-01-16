package de.placeblock.commandapi.core.parameter;

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

    @Override
    public Integer parse(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) {
        String integerWord = context.getReader().readUnquotedString();
        try {
            return Integer.parseInt(integerWord);
        } catch (NumberFormatException ignored) {
            context.getErrors().put(command, new CommandSyntaxException(Texts.inferior("Du musst eine <color:negative>Zahl angeben")));
        }
        return null;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, Integer> command) {
        List<String> suggestions = new ArrayList<>();
        Integer parsedParameter = command != null ? context.getParameter(command.getName(), Integer.class) : null;
        for (int i = 0; i < 10; i++) {
            suggestions.add((parsedParameter != null ? parsedParameter.toString() : "") + i);
        }
        return suggestions;
    }

}
