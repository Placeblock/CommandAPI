package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import io.schark.design.texts.Texts;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    public ParsedValue<E> parse(ParseContext<S> context, ParameterTreeCommand<S, E> command) {
        ParsedValue<String> word = context.getReader().readUnquotedString();
        System.out.println("'"+word.getValue()+"'");
        if (word.isInvalid()) {
            return new ParsedValue<>(null, word.getString(), word.getSyntaxException());
        }
        try {
            E enumValue = Enum.valueOf(enumClass, word.getValue().toUpperCase());
            return new ParsedValue<>(enumValue, word.getString(), null);
        } catch (IllegalArgumentException ex) {
            return new ParsedValue<>(null, word.getString(), new CommandSyntaxException(Texts.inferior("Das Argument <color:primary>" + word + " <color:negative>existiert nicht<color:inferior>.")));
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, E> command) {
        ParsedValue<E> parsedValue = context.getParameter(command.getName(), this.enumClass);
        String partial = parsedValue == null ? "" : parsedValue.getString();
        ArrayList<String> suggestions = new ArrayList<>();
        for (E enumValue : this.enumValues) {
            String displayName;
            if (enumValue instanceof CommandEnum commandEnumValue) {
                displayName = commandEnumValue.getDisplayName();
            } else {
                displayName = enumValue.name();
            }
            if (displayName.toLowerCase().startsWith(partial.toLowerCase())) {
                suggestions.add(displayName);
            }
        }
        return suggestions;
    }
}
