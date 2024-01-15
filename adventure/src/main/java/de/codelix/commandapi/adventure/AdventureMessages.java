package de.codelix.commandapi.adventure;

import de.codelix.commandapi.core.exception.NoPermissionParseException;
import de.codelix.commandapi.core.message.CommandMessages;
import de.codelix.commandapi.core.parameter.exceptions.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class AdventureMessages extends CommandMessages<TextComponent> {

    public AdventureMessages() {
        add(NoPermissionParseException.class, (e) -> Component.text("No permission"));
        add(MarkMissingParseException.class, (e) -> Component.text("Quotation mark missing: " + e.getInput()));
        add(InvalidIntegerParseException.class, (e) -> Component.text("Invalid Integer " + e.getInput()));
        add(InvalidDoubleParseException.class, (e) -> Component.text("Invalid float " + e.getInput()));
        add(IntegerTooSmallParseException.class, (e) -> Component.text("Integer " + e.getValue() + " is too small (Min " + e.getMin() + ")"));
        add(DoubleTooSmallParseException.class, (e) -> Component.text("Double " + e.getValue() + " is too small (Min " + e.getMin() + ")"));
        add(IntegerTooLargeParseException.class, (e) -> Component.text("Integer " + e.getValue() + " is too large (Max " + e.getMax() + ")"));
        add(DoubleTooLargeParseException.class, (e) -> Component.text("Double " + e.getValue() + " is too large (Max " + e.getMax() + ")"));
    }
}
