// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception;

import de.placeblock.commandapi.util.Message;
import de.placeblock.commandapi.util.StringReader;

import java.util.function.Function;

public class DynamicCommandExceptionType implements CommandExceptionType {
    private final Function<Object, Message> function;

    public DynamicCommandExceptionType(final Function<Object, Message> function) {
        this.function = function;
    }

    public CommandSyntaxException create(final Object arg) {
        return new CommandSyntaxException(this, function.apply(arg));
    }

    public CommandSyntaxException createWithContext(final StringReader reader, final Object arg) {
        return new CommandSyntaxException(this, function.apply(arg), reader.getString(), reader.getCursor());
    }
}
