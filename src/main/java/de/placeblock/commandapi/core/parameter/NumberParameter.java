package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import io.schark.design.texts.Texts;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@RequiredArgsConstructor
public abstract class NumberParameter<S, T extends Number> implements Parameter<S, T> {
    protected final T min;
    protected final T max;

    protected T checkNumber(T parsed) throws CommandSyntaxException {
        if (parsed == null) return null;
        double numberDouble = parsed.doubleValue();
        if (numberDouble < this.min.doubleValue()) {
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+parsed+" <color:inferior>ist <color:negative>zu klein<color:inferior>. Das Minimum ist <color:negative>" + this.min));
        }
        if (numberDouble > this.max.doubleValue()) {
            throw new CommandSyntaxException(Texts.inferior("Die angegebene Zahl <color:negative>"+parsed+" <color:inferior>ist <color:negative>zu groß<color:inferior>. Das Maximum ist <color:negative>" + this.max));
        }
        return parsed;
    }
}
