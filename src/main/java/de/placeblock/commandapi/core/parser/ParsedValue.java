package de.placeblock.commandapi.core.parser;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Author: Placeblock
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ParsedValue<T> {
    private T value;
    private String string;
    private CommandSyntaxException syntaxException;

    public ParsedValue(String string) {
        this.string = string;
    }

    public T getParameter() {
        return value;
    }

    public <CT>  CT getParameter(Class<CT> type) {
        return type.cast(value);
    }

    public boolean hasException() {
        return this.syntaxException != null;
    }

    public boolean isValid() {
        return this.syntaxException == null && this.value == null;
    }
}
