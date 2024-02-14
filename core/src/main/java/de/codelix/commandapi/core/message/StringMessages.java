package de.codelix.commandapi.core.message;

import de.codelix.commandapi.core.exception.*;

public class StringMessages extends CommandMessages<String> {

    public StringMessages() {
        add(EndOfCommandParseException.class,  (e) -> "You provided too many arguments.");
        add(InvalidArgumentParseException.class, (e) -> "Invalid argument for " + e.getNode().getDisplayNameSafe() + " provided.");
        add(InvalidLiteralParseException.class, (e) -> "Invalid literal " + e.getProvided() + ". Did you mean " + e.getNode().getDisplayNameSafe() + "?");
        add(NoPermissionParseException.class, (e) -> "No permission.");
        add(NoRunParseException.class, (e) -> "This is not a valid command");
    }

}
