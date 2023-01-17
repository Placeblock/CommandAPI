package de.placeblock.commandapi.core.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class ParsedParameter<T> {
    private final T parsed;
    private final String parsedString;

    public T getParameter() {
        return parsed;
    }

    public <CT>  CT getParameter(Class<CT> type) {
        return type.cast(parsed);
    }
}
