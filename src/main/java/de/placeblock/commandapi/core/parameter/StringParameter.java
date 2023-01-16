package de.placeblock.commandapi.core.parameter;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import lombok.Getter;
import org.bukkit.command.CommandException;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
public class StringParameter<S> implements Parameter<S, String> {
    private static final char SYNTAX_DOUBLE_QUOTE = '"';
    private static final char SYNTAX_SINGLE_QUOTE = '\'';

    @Getter
    private final StringType type;

    private StringParameter(StringType type) {
        this.type = type;
    }

    @Override
    public String parse(ParseContext<S> context, ParameterTreeCommand<S, String> command) throws CommandException {
        return null;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context , ParameterTreeCommand<S, String> command) {
        return new ArrayList<>();
    }
}

