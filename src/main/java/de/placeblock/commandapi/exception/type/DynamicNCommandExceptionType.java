// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception.type;

import de.placeblock.commandapi.exception.CommandSyntaxException;
import net.kyori.adventure.text.TextComponent;

public class DynamicNCommandExceptionType implements CommandExceptionType {
    private final Function function;

    public DynamicNCommandExceptionType(final Function function) {
        this.function = function;
    }

    public CommandSyntaxException create(final Object a, final Object... args) {
        return new CommandSyntaxException(this, function.apply(args));
    }

    public interface Function {
        TextComponent apply(Object[] args);
    }
}
