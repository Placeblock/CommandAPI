package de.placeblock.commandapi.core.parser;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
@Getter
public class ParsedCommand<S> extends ParameterHolder {
    private final StringReader reader;
    private final List<TreeCommand<S>> parsedTreeCommands;
    private final Map<TreeCommand<S>, CommandSyntaxException> exceptions;

    public ParsedCommand(StringReader reader) {
        super();
        this.reader = reader;
        this.parsedTreeCommands = new ArrayList<>();
        this.exceptions = new HashMap<>();
    }

    public ParsedCommand(ParsedCommand<S> parsedCommand) {
        super(parsedCommand.getParsedParameters());
        this.reader = new StringReader(parsedCommand.getReader());
        this.parsedTreeCommands = new ArrayList<>(parsedCommand.getParsedTreeCommands());
        this.exceptions = new HashMap<>(parsedCommand.getExceptions());
    }

    public TreeCommand<S> getLastParsedTreeCommand() {
        if (this.parsedTreeCommands.size() == 0) return null;
        return this.parsedTreeCommands.get(this.parsedTreeCommands.size() - 1);
    }

    public void addParsedParameter(String name, Object parameter) {
        this.parsedParameters.put(name, parameter);
    }


    public void addException(TreeCommand<S> command, CommandSyntaxException exception) {
        this.exceptions.put(command, exception);
    }

}
