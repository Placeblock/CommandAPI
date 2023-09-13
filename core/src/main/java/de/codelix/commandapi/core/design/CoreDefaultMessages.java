package de.codelix.commandapi.core.design;

import de.codelix.commandapi.core.exception.*;
import net.kyori.adventure.text.Component;

@SuppressWarnings("unused")
public class CoreDefaultMessages implements DefaultMessages{

    @Override
    public void register(CommandDesign design) {
        design.register(QuotedStringRequiredException.class, e -> Component.text("Quoted String required for ").append(e.dName()));
        design.register(BooleanRequiredException.class, e -> Component.text("Boolean required for ").append(e.dName()));
        design.register(DecimalRequiredException.class, e -> Component.text("Decimal required for ").append(e.dName()));
        design.register(IntegerRequiredException.class, e -> Component.text("Integer required for ").append(e.dName()));
        design.register(InvalidBooleanException.class, e -> Component.text("Invalid Boolean '" + e.getBool() + "' for ").append(e.dName()));
        design.register(InvalidDecimalException.class, e -> Component.text("Invalid Decimal '" + e.getDecimal() + "' for ").append(e.dName()));
        design.register(InvalidIntegerException.class, e -> Component.text("Invalid Integer '" + e.getNumber() + "' for ").append(e.dName()));
        design.register(InvalidEscapeCharException.class, e -> Component.text("Cannot escape '" + e.getCharacter() + "' in ").append(e.dName()));
        design.register(InvalidParameterValueException.class, e -> Component.text("Invalid Value '" + e.getParameter() + "' for ").append(e.dName()));
        design.register(NoEndQuoteException.class, e -> Component.text("No end-quote found for ").append(e.dName()));
        design.register(NumberTooBigException.class, e -> Component.text("Number " + e.getNumber() + " is too big for ").append(e.dName()).append(Component.text(". Maximum is " + e.getMax())));
        design.register(NumberTooSmallException.class, e -> Component.text("Number " + e.getNumber() + " is too small for ").append(e.dName()).append(Component.text(". Minimum is " + e.getMin())));
        design.register(EmptyGreedyException.class, e -> Component.text("Empty Greedy String is invalid for ").append(e.dName()));
        design.register(NotEnoughDimensionsException.class, e -> Component.text("Not enough values provided for ").append(e.dName()).append(Component.text(" (" + e.getProvided() + "/"+e.getDimensions()+")")));
    }

}
