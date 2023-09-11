package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.exception.EmptyGreedyException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.parser.StringReader;
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
    public String parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        StringReader reader = command.getReader();
        String parsedText;
        if (type == StringType.GREEDY_PHRASE) {
            String remaining = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            if (remaining.equals("")) {
                throw new EmptyGreedyException();
            }
            return remaining;
        }
        return this.type == StringType.SINGLE_WORD ? reader.readUnquotedString() : reader.readString();
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {}
}

