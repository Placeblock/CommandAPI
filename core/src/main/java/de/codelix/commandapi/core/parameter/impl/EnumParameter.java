package de.codelix.commandapi.core.parameter.impl;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EnumParameter<T extends Enum<T>, S> implements Parameter<T, S> {
    private final Class<T> enumClass;
    private final T[] enumValues;

    @Override
    public T parse(ParseContext<S> ctx, ParsedCommand<S> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        // Evtl messages auch für übergeordnete exceptions
        // Dann könnte man ne InvalidParameterValueParseException oder so machen
        // Die dann generell mal nen String entgegen nimmt und ne generische nachricht
        // anzeigt. Enumparameter könnte spezifischere Exceptions machen die
        // die Invalid Parameter Exception erweitern.
        assert next != null;
        try {
            if (CommandEnum.class.isAssignableFrom(this.enumClass)) {
                for (T enumValue : this.enumValues) {
                    if (next.equals(((CommandEnum) enumValue).getDisplayName())) {
                        return enumValue;
                    }
                }
                throw new InvalidParameterValueException(word);
            } else {
                return Enum.valueOf(this.enumClass, next.toUpperCase());
            }
        } catch (IllegalArgumentException ex) {
            throw new InvalidParameterValueException(word);
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> ctx, ParsedCommand<S> cmd) {
        String next = ctx.getRemaining();
        List<String> suggestions = new ArrayList<>();
        for (T enumValue : this.enumValues) {
            String displayName;
            if (enumValue instanceof CommandEnum commandEnumValue) {
                displayName = commandEnumValue.getDisplayName();
            } else {
                displayName = enumValue.name();
            }
            if (displayName.toLowerCase().startsWith(next.toLowerCase())) {
                suggestions.add(displayName);
            }
        }
        return suggestions;
    }
}
