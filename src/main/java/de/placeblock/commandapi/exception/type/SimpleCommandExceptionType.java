// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception.type;

import de.placeblock.commandapi.exception.CommandSyntaxException;
import net.kyori.adventure.text.TextComponent;

public class SimpleCommandExceptionType implements CommandExceptionType {
    private final TextComponent message;

    public SimpleCommandExceptionType(final TextComponent message) {
        this.message = message;
    }

    public CommandSyntaxException create() {
        return new CommandSyntaxException(this, message);
    }

    @Override
    public String toString() {
        return message.toString();
    }
}
