package de.codelix.commandapi.core.design;

import de.codelix.commandapi.core.exception.*;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public class CoreDefaultMessages implements DefaultMessages{

    @Override
    public void register(CommandDesign design) {
        design.register(QuotedStringRequiredException.class, e -> Component.text("Quoted String required for " + e.getTreeCommand().getName()));
        design.register(BooleanRequiredException.class, e -> Component.text("Boolean required for " + e.getTreeCommand().getName()));
        design.register(DecimalRequiredException.class, e -> Component.text("Decimal required for " + e.getTreeCommand().getName()));
        design.register(IntegerRequiredException.class, e -> Component.text("Integer required for " + e.getTreeCommand().getName()));
        design.register(InvalidBooleanException.class, e -> Component.text("Invalid Boolean '" + e.getBool() + "' for " + e.getTreeCommand().getName()));
        design.register(InvalidDecimalException.class, e -> Component.text("Invalid Decimal '" + e.getDecimal() + "' for " + e.getTreeCommand().getName()));
        design.register(InvalidIntegerException.class, e -> Component.text("Invalid Integer '" + e.getNumber() + "' for " + e.getTreeCommand().getName()));
        design.register(InvalidEscapeCharException.class, e -> Component.text("Cannot escape '" + e.getCharacter() + "' in " + e.getTreeCommand().getName()));
        design.register(InvalidParameterValueException.class, e -> Component.text("Invalid Value '" + e.getParameter() + "' for " + e.getTreeCommand().getName()));
        design.register(NoEndQuoteException.class, e -> Component.text("No end-quote found for " + e.getTreeCommand().getName()));
        design.register(NumberTooBigException.class, e -> Component.text("Number " + e.getNumber() + " is too big for " + e.getTreeCommand().getName() + ". Maximum is " + e.getMax()));
        design.register(NumberTooSmallException.class, e -> Component.text("Number " + e.getNumber() + " is too small for " + e.getTreeCommand().getName() + ". Minimum is " + e.getMin()));
        design.register(EmptyGreedyException.class, e -> Component.text("Empty Greedy String is invalid for " + e.getTreeCommand().getName()));
        design.register(NotEnoughDimensionsException.class, e -> Component.text("Not enough values provided for " + e.getTreeCommand().getName() + " (" + e.getProvided() + "/"+e.getDimensions()+")"));
    }

}
