package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.parser.StringReader;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
        return new StringParameter<S>(StringType.SINGLE_WORD);
    }

    public static <S> StringParameter<S> string() {
        return new StringParameter<S>(StringType.QUOTABLE_PHRASE);
    }

    public static <S> StringParameter<S> greedyString() {
        return new StringParameter<>(StringType.GREEDY_PHRASE);
    }

    @Override
    public ParsedValue<?> parse(ParseContext<S> context, ParameterTreeCommand<S, String> command) {
        StringReader reader = context.getReader();
        String parsedText;
        if (type == StringType.GREEDY_PHRASE) {
            reader.setCursor(reader.getTotalLength());
            String remaining = reader.getRemaining();
            return new ParsedValue<>(remaining, remaining, null);
        } else if (type == StringType.SINGLE_WORD) {
            return reader.readUnquotedString();
        } else {
            return reader.readString();
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, @Nullable ParameterTreeCommand<S, String> command) {
        return new ArrayList<>();
    }
}

