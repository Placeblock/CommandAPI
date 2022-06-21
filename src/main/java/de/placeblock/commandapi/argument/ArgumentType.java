package de.placeblock.commandapi.argument;

import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringReader;

public interface ArgumentType<T> {

    T parse(final StringReader reader) throws CommandSyntaxException;

}
