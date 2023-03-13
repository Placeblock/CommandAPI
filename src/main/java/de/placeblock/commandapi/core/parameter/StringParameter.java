package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.parser.StringReader;
import io.schark.design.texts.Texts;
import lombok.Getter;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public class StringParameter<S> implements Parameter<S, String> {
    @Getter
    private final StringType type;

    private StringParameter(StringType type) {
        this.type = type;
    }

    public static <S> StringParameter<S> word() {
        return new StringParameter<>(StringType.SINGLE_WORD);
    }

    public static <S> StringParameter<S> string() {
        return new StringParameter<>(StringType.QUOTABLE_PHRASE);
    }

    public static <S> StringParameter<S> greedyString() {
        return new StringParameter<>(StringType.GREEDY_PHRASE);
    }

    @Override
    public String parse(ParsedCommand<S> command, S source) throws CommandSyntaxException {
        StringReader reader = command.getReader();
        String parsedText;
        if (type == StringType.GREEDY_PHRASE) {
            String remaining = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            if (remaining.equals("")) {
                throw  new CommandSyntaxException(Texts.inferior("Ein <color:primary>leerer String <color:inferior>ist als greedy Argument <color:negative>nicht erlaubt"));
            }
            return remaining;
        }
        return this.type == StringType.SINGLE_WORD ? reader.readUnquotedString() : reader.readString();
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {}
}

