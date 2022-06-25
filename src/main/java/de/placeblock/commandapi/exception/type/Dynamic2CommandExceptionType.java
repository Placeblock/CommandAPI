// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception.type;

import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringReader;
import net.kyori.adventure.text.TextComponent;

public class Dynamic2CommandExceptionType implements CommandExceptionType {
    private final Function function;

    public Dynamic2CommandExceptionType(final Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(final Object a, final Object b) {
        return new CommandSyntaxException(this, function.apply(a, b));
    }

    public interface Function {
        TextComponent apply(Object a, Object b);
    }
}
