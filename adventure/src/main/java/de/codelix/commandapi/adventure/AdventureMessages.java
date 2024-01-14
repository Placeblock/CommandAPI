package de.codelix.commandapi.adventure;

import de.codelix.commandapi.core.exception.EndOfCommandParseException;
import de.codelix.commandapi.core.message.CommandMessages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class AdventureMessages extends CommandMessages<TextComponent> {

    public AdventureMessages() {
        add(EndOfCommandParseException.class, (e) -> Component.text("No permission"));
    }
}
