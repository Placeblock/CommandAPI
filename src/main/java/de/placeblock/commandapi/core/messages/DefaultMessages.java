package de.placeblock.commandapi.core.messages;

import de.placeblock.commandapi.core.exception.*;
import net.kyori.adventure.text.Component;

public class DefaultMessages extends Messages{

    public DefaultMessages() {
        this.register(QuotedStringRequiredException.class, e -> Component.text("Quoted String required for " + e.getTreeCommand().getName()));
        this.register(BooleanRequiredException.class, e -> Component.text("Boolean required for " + e.getTreeCommand().getName()));
        this.register(DecimalRequiredException.class, e -> Component.text("Decimal required for " + e.getTreeCommand().getName()));
        this.register(IntegerRequiredException.class, e -> Component.text("Integer required for " + e.getTreeCommand().getName()));
        this.register(InvalidBooleanException.class, e -> Component.text("Invalid Boolean '" + e.getBool() + "' for " + e.getTreeCommand().getName()));
        this.register(InvalidDecimalException.class, e -> Component.text("Invalid Decimal '" + e.getDecimal() + "' for " + e.getTreeCommand().getName()));
        this.register(InvalidIntegerException.class, e -> Component.text("Invalid Integer '" + e.getNumber() + "' for " + e.getTreeCommand().getName()));
        this.register(InvalidEscapeCharException.class, e -> Component.text("Cannot escape '" + e.getCharacter() + "' in " + e.getTreeCommand().getName()));
        this.register(InvalidParameterValueException.class, e -> Component.text("Invalid Value '" + e.getParameter() + "' for " + e.getTreeCommand().getName()));
        this.register(NoEndQuoteException.class, e -> Component.text("No end-quote found for " + e.getTreeCommand().getName()));
        this.register(NumberTooBigException.class, e -> Component.text("Number " + e.getNumber() + " is too big for " + e.getTreeCommand().getName() + ". Maximum is " + e.getMax()));
        this.register(NumberTooSmallException.class, e -> Component.text("Number " + e.getNumber() + " is too small for " + e.getTreeCommand().getName() + ". Minimum is " + e.getMin()));
        this.register(EmptyGreedyException.class, e -> Component.text("Empty Greedy String is invalid for " + e.getTreeCommand().getName()));
    }

}
