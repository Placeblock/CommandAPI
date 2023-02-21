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
public class ParsedCommand<S> {
    private final StringReader reader;
    private final Map<String, Object> parsedParameters;
    private final List<TreeCommand<S>> parsedTreeCommands;
    private final Map<TreeCommand<S>, CommandSyntaxException> exceptions;

    public ParsedCommand(StringReader reader) {
        this.reader = reader;
        this.parsedParameters = new HashMap<>();
        this.parsedTreeCommands = new ArrayList<>();
        this.exceptions = new HashMap<>();
    }

    public ParsedCommand(ParsedCommand<S> parsedCommand) {
        this.reader = new StringReader(parsedCommand.getReader());
        this.parsedParameters = new HashMap<>(parsedCommand.getParsedParameters());
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

    public Object getParsedParameter(String name) {
        return this.parsedParameters.get(name);
    }

    public <T> T getParsedParameter(String name, Class<T> type) {
        //noinspection unchecked
        return (T) this.parsedParameters.get(name);
    }

    public <T> T getParsedParameterOrDefault(String name, Class<T> type, T defaultValue) {
        T value = this.getParsedParameter(name, type);
        return value != null ? value : defaultValue;
    }

    public void addException(TreeCommand<S> command, CommandSyntaxException exception) {
        this.exceptions.put(command, exception);
    }

}
