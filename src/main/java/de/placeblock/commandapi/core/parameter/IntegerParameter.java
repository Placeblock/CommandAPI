package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.Util;
import de.placeblock.commandapi.core.parser.ParseContext;
import io.schark.design.texts.Texts;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Author: Placeblock
 */
@RequiredArgsConstructor
public class IntegerParameter<S> implements Parameter<S, Integer> {

    @Override
    public Integer parse(ParseContext<S> context) {
        int nextWordIndex = Util.readWord(context);
        String integerWord = context.getText().substring(context.getCursor(), nextWordIndex);
        try {
            Integer number = Integer.parseInt(integerWord);
            context.setCursor(nextWordIndex);
            return number;
        } catch (NumberFormatException ignored) {
            context.setError(Texts.inferior("Du musst eine <color:negative>Zahl angeben"));
        }
        return null;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        return List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    }

}
