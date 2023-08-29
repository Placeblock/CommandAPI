package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.exception.NumberTooBigException;
import de.placeblock.commandapi.core.exception.NumberTooSmallException;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@RequiredArgsConstructor
public abstract class NumberParameter<S, T extends Number> implements Parameter<S, T> {
    protected final T min;
    protected final T max;

    protected T checkNumber(T parsed) throws CommandParseException {
        if (parsed == null) return null;
        double numberDouble = parsed.doubleValue();
        if (numberDouble < this.min.doubleValue()) {
            throw new NumberTooSmallException(numberDouble, this.min);
        }
        if (numberDouble > this.max.doubleValue()) {
            throw new NumberTooBigException(numberDouble, this.max);
        }
        return parsed;
    }
}
