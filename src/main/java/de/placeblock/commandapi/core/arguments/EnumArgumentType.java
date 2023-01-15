package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.texts.Texts;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EnumArgumentType<S, E extends Enum<E>> implements ArgumentType<S, E> {
    private final Class<E> enumClass;
    private final E[] enumValues;

    @Override
    public E parse(S source, StringReader reader) throws CommandException {
        String word = reader.readUnquotedString();
        try {
            return Enum.valueOf(enumClass, word.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new CommandSyntaxException(Texts.inferior("Das Argument <color:primary>" + word + " <color:negative>existiert nicht<color:inferior>."));
        }
    }

    @Override
    public List<String> listSuggestions(CommandContext<S> context, String partial) {
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
