package de.placeblock.commandapi.core.exception;

import net.kyori.adventure.text.Component;

public class CommandHelpException extends CommandSyntaxException {
    public CommandHelpException() {
        super(Component.empty());
    }
}
