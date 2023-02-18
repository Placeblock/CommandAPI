package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.parser.StringReader;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import io.schark.design.texts.Texts;
import lombok.Getter;

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
        return new StringParameter<>(StringType.SINGLE_WORD);
    }

    public static <S> StringParameter<S> string() {
        return new StringParameter<>(StringType.QUOTABLE_PHRASE);
    }

    public static <S> StringParameter<S> greedyString() {
        return new StringParameter<>(StringType.GREEDY_PHRASE);
    }

    @Override
    public ParsedValue<String> parse(ParseContext<S> context, ParameterTreeCommand<S, String> command) {
        StringReader reader = context.getReader();
        String parsedText;
        if (type == StringType.GREEDY_PHRASE) {
            String remaining = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
            if (remaining.equals("")) {
                return new ParsedValue<>(null, "", new CommandSyntaxException(Texts.inferior("Ein <color:primary>leerer String <color:inferior>ist als greedy Argument <color:negative>nicht erlaubt")));
            }
            return new ParsedValue<>(remaining, remaining, null);
        }
        return this.type == StringType.SINGLE_WORD ? reader.readUnquotedString() : reader.readString();
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, String> command) {
        return new ArrayList<>();
    }
}

