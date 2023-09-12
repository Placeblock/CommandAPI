package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.exception.NumberTooBigException;
import de.codelix.commandapi.core.exception.NumberTooSmallException;
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
        checkBounds(numberDouble, this.min.doubleValue(), this.max.doubleValue());
        return parsed;
    }

    public static void checkBounds(double numberDouble, double min, double max) throws NumberTooSmallException, NumberTooBigException {
        if (numberDouble < min) {
            throw new NumberTooSmallException(numberDouble, min);
        }
        if (numberDouble > max) {
            throw new NumberTooBigException(numberDouble, max);
        }
    }
}
