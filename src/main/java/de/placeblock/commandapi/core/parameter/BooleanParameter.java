package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;

import java.util.List;

/**
 * Author: Placeblock
 */
public class BooleanParameter<S> implements Parameter<S, Boolean>{
    public static <S> BooleanParameter<S> bool() {
        return new BooleanParameter<>();
    }

    @Override
    public Boolean parse(ParsedCommand<S> command) throws CommandSyntaxException {
        return command.getReader().readBoolean();
    }

    @Override
    public List<String> getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        return Parameter.startsWith(List.of("true", "false"), partial);
    }
}
