package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;

import java.util.List;

/**
 * Author: Placeblock
 */
public class BooleanParameter<S> implements Parameter<S, Boolean>{
    public static <S> BooleanParameter<S> bool() {
        return new BooleanParameter<>();
    }

    @Override
    public ParsedValue<Boolean> parse(ParseContext<S> context, ParameterTreeCommand<S, Boolean> command) {
        return context.getReader().readBoolean();
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context, ParameterTreeCommand<S, Boolean> command) {
        return this.startsWith(List.of("true", "false"), context.getParameter(command.getName()).getString());
    }
}
