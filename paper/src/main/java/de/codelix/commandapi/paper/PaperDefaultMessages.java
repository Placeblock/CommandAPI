package de.codelix.commandapi.paper;

import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.core.design.DefaultMessages;
import de.codelix.commandapi.paper.exception.InvalidPlayerException;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public class PaperDefaultMessages implements DefaultMessages {
    public void register(CommandDesign design) {
        design.register(InvalidPlayerException.class, e -> Component.text("Player " + e.getName() + " not found"));
    }
}
