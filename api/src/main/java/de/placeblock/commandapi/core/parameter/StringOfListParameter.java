package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.exception.InvalidParameterValueException;
import de.placeblock.commandapi.core.parser.ParsedCommandBranch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class StringOfListParameter<S> implements Parameter<S, String> {
    private final List<String> values;

    @SuppressWarnings("unused")
    public static <S> StringOfListParameter<S> stringoflist(List<String> values) {
        return new StringOfListParameter<>(values);
    }

    @Override
    public String parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        String word = command.getReader().readUnquotedString();
        if (!this.values.contains(word)) {
            throw new InvalidParameterValueException(word);
        }
        return word;
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        suggestionBuilder.withSuggestions(Parameter.startsWith(this.values, partial));
    }
}
