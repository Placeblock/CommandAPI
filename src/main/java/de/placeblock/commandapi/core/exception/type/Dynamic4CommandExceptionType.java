// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.core.exception.type;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import net.kyori.adventure.text.TextComponent;

public class Dynamic4CommandExceptionType implements CommandExceptionType {
    private final Function function;

    public Dynamic4CommandExceptionType(final Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(final Object a, final Object b, final Object c, final Object d) {
        return new CommandSyntaxException(this, function.apply(a, b, c, d));
    }

    public interface Function {
        TextComponent apply(Object a, Object b, Object c, Object d);
    }
}
