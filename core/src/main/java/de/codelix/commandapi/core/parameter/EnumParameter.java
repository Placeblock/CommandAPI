package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.exception.InvalidParameterValueException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
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
    public E parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        String word = command.getReader().readUnquotedString();
        try {
            if (CommandEnum.class.isAssignableFrom(this.enumClass)) {
                for (E enumValue : this.enumValues) {
                    if (word.equals(((CommandEnum) enumValue).getDisplayName())) {
                        return enumValue;
                    }
                }
                throw new InvalidParameterValueException(word);
            } else {
                return Enum.valueOf(this.enumClass, word.toUpperCase());
            }
        } catch (IllegalArgumentException ex) {
            throw new InvalidParameterValueException(word);
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
