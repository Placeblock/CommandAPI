package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;

import java.util.List;

/**
 * Author: Placeblock
 */
public class BooleanParameter<S> implements Parameter<S, Boolean>{
    public static <S> BooleanParameter<S> bool() {
        return new BooleanParameter<>();
    }

    @Override
    public Boolean parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        return command.getReader().readBoolean();
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        suggestionBuilder.withSuggestions(Parameter.startsWith(List.of("true", "false"), partial));
    }
}
