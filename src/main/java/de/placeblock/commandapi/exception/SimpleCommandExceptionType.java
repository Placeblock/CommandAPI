// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception;

import de.placeblock.commandapi.util.Message;
import de.placeblock.commandapi.util.StringReader;

public class SimpleCommandExceptionType implements CommandExceptionType {
    private final Message message;

    public SimpleCommandExceptionType(final Message message) {
        this.message = message;
    }

    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, message);
    }

    public CommandSyntaxException createWithContext(final StringReader reader) {
        return new CommandSyntaxException(this, message, reader.getString(), reader.getCursor());
    }

    @Override
    public String toString() {
        return message.getString();
    }
}
