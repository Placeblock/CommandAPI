package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParseContext;
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

    @Override
    public E parse(ParseContext<S> context, ParameterTreeCommand<S, E> command) throws CommandException {
        String word = context.getReader().readUnquotedString();
        try {
            return Enum.valueOf(enumClass, word.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CommandSyntaxException(Texts.inferior("Das Argument <color:primary>" + word + " <color:negative>existiert nicht<color:inferior>."));
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, E> command) {
        String partial = context.getReader().getRemaining().trim();
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
