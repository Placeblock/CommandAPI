package de.codelix.commandapi.velocity;

import de.codelix.commandapi.adventure.AdventureMessages;
import de.codelix.commandapi.velocity.parameter.InvalidPlayerParseException;
import net.kyori.adventure.text.Component;

public class VelocityMessages extends AdventureMessages {
    public VelocityMessages() {
        add(InvalidPlayerParseException.class, (e) -> Component.text("Invalid Player " + e.getInput() + ". Is the player online?"));
    }
}
