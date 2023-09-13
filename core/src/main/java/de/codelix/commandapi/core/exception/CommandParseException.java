package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.CommandNode;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
@Setter
@Getter
public class CommandParseException extends CommandException {
    private CommandNode<?> commandNode;
    public TextComponent dName() {
        return this.commandNode.getDisplayName();
    }
}
