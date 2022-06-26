// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.core.exception.type;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import net.kyori.adventure.text.TextComponent;

import java.util.function.Function;

public class DynamicCommandExceptionType implements CommandExceptionType {
    private final Function<Object, TextComponent> function;

    public DynamicCommandExceptionType(final Function<Object, TextComponent> function) {
        this.function = function;
    }

    public CommandSyntaxException create(final Object arg) {
        return new CommandSyntaxException(this, function.apply(arg));
    }
}
