package de.codelix.commandapi.paper;

import de.codelix.commandapi.adventure.AdventureMessages;
import de.codelix.commandapi.paper.parameter.InvalidPlayerParseException;
import net.kyori.adventure.text.Component;

public class PaperMessages extends AdventureMessages {

    public PaperMessages() {
        add(InvalidPlayerParseException.class, (e) -> Component.text("Invalid player " + e.getInput() + ". Is the player online?"));
    }

}
