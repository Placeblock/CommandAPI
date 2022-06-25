// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package de.placeblock.commandapi.exception;

import de.placeblock.commandapi.exception.type.CommandExceptionType;
import net.kyori.adventure.text.TextComponent;

public class CommandSyntaxException extends Exception {
    public static boolean ENABLE_COMMAND_STACK_TRACES = true;
    public static BuiltInExceptionProvider BUILT_IN_EXCEPTIONS = new BuiltInExceptions();

    private final CommandExceptionType type;
    private final TextComponent message;

    public CommandSyntaxException(final CommandExceptionType type, final TextComponent message) {
        super(message.content(), null, ENABLE_COMMAND_STACK_TRACES, ENABLE_COMMAND_STACK_TRACES);
        this.type = type;
        this.message = message;
    }

    public TextComponent getRawMessage() {
        return message;
    }


    public CommandExceptionType getType() {
        return type;
    }

}
