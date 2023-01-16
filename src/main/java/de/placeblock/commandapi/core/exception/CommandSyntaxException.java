package de.placeblock.commandapi.core.exception;

import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public class CommandSyntaxException extends CommandException {
    public CommandSyntaxException(TextComponent message) {
        super(message);
    }
}
