package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import io.schark.design.texts.Texts;
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
    public String parse(ParsedCommand<S> command) throws CommandSyntaxException {
        String word = command.getReader().readUnquotedString();
        if (this.values.contains(word)) {
            throw new CommandSyntaxException(Texts.inferior("Du hast ein <color:negative>falsches Argument angegeben"));
        }
        return word;
    }

    @Override
    public List<String> getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        return Parameter.startsWith(this.values, partial);
    }
}
