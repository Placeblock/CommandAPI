package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.Util;
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
        int nextWordIndex = Util.readWord(context);
        String integerWord = context.getText().substring(context.getCursor(), nextWordIndex);
        try {
            Integer number = Integer.parseInt(integerWord);
            context.setCursor(nextWordIndex);
            return number;
        } catch (NumberFormatException ignored) {
            context.getErrors().put(this, Texts.inferior("Du musst eine <color:negative>Zahl angeben"));
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
