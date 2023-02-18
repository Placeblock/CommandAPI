package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
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
    public ParsedValue<String> parse(ParseContext<S> context, ParameterTreeCommand<S, String> command) {
        ParsedValue<String> word = context.getReader().readUnquotedString();
        if (word.isInvalid()){
            return word;
        }
        if (this.values.contains(word.getValue())) {
            word.setSyntaxException(new CommandSyntaxException(Texts.inferior("Du hast ein <color:negative>falsches Argument angegeben")));
        }
        return word;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, String> command) {
        ParsedValue<String> parsedValue = context.getParameter(command.getName(), String.class);
        String partial = parsedValue.getString();
        return this.startsWith(this.values, partial);
    }
}
