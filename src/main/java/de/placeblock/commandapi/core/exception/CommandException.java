package de.placeblock.commandapi.core.exception;

import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public class CommandException extends Exception {

    private final TextComponent message;

    public CommandException(TextComponent message) {
        super(message.content());
        this.message = message;
    }

    public TextComponent getTextMessage() {
        return this.message;
    }

}
