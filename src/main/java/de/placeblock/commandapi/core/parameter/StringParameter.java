package de.placeblock.commandapi.core.parameter;

import com.mojang.brigadier.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedParameter;
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
    public ParsedParameter<?> parse(ParseContext<S> context, ParameterTreeCommand<S, String> command) throws CommandException {
        StringReader reader = context.getReader();
        String parsedText;
        if (type == StringType.GREEDY_PHRASE) {
            parsedText = reader.getRemaining();
            reader.setCursor(reader.getTotalLength());
        } else if (type == StringType.SINGLE_WORD) {
            parsedText = reader.readUnquotedString();
        } else {
            parsedText = reader.readString();
        }
        return new ParsedParameter<>(parsedText, parsedText);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, @Nullable ParameterTreeCommand<S, String> command) {
        return new ArrayList<>();
    }
}

