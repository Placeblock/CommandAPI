package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import io.schark.design.texts.Texts;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@RequiredArgsConstructor
public class EnumParameter<S, E extends Enum<E>> implements Parameter<S, E> {
    private final Class<E> enumClass;
    private final E[] enumValues;

    @SuppressWarnings("unused")
    public static <S, E extends Enum<E>> EnumParameter<S, E> enumparam(Class<E> enumClass, E[] enumValues) {
        return new EnumParameter<>(enumClass, enumValues);
    }

    @Override
    public E parse(ParsedCommand<S> command, S source) throws CommandSyntaxException {
        String word = command.getReader().readUnquotedString();
        try {
            return Enum.valueOf(this.enumClass, word.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CommandSyntaxException(Texts.inferior("Das Argument <color:primary>" + word + " <color:negative>existiert nicht<color:inferior>."));
        }
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        for (E enumValue : this.enumValues) {
            String displayName;
            if (enumValue instanceof CommandEnum commandEnumValue) {
                displayName = commandEnumValue.getDisplayName();
            } else {
                displayName = enumValue.name();
            }
            if (displayName.toLowerCase().startsWith(partial.toLowerCase())) {
                suggestionBuilder.withSuggestion(displayName);
            }
        }
    }
}
