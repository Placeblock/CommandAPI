package de.placeblock.commandapi.core.exception;

import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

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
