package de.placeblock.commandapi.core.parser;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class ParseContext<S> {

    private final StringReader reader;
    private final S source;
    private final Map<String, ParsedParameter<?>> parameters = new HashMap<>();
    @Setter
    private Map<TreeCommand<S>, CommandException> errors = new HashMap<>();
    @Setter
    private List<TreeCommand<S>> parsedCommands = new ArrayList<>();

    public ParseContext(String text, S source) {
        this(new StringReader(text), source);
    }

    public TreeCommand<S> getLastParsedCommand() {
        if (this.parsedCommands.size() == 0) return null;
        return this.parsedCommands.get(this.parsedCommands.size() - 1);
    }

    public void addParameter(String name, ParsedParameter<?> parameter) {
        this.parameters.put(name, parameter);
    }

    public void addError(TreeCommand<S> command, CommandException exception) {this.errors.put(command, exception);}

    public void addParsedCommand(TreeCommand<S> command) {this.parsedCommands.add(command);}

    public <T> T getParameter(String name, Class<T> type) {
        if (!this.parameters.containsKey(name)) return null;
        return type.cast(this.parameters.get(name).getParameter(type));
    }

    public ParsedParameter<?> getParsedParameter(String name) {
        return this.parameters.get(name);
    }

    public boolean hasError(TreeCommand<S> command) {
        return this.errors.containsKey(command);
    }

    public boolean isParsedToEnd() {
        return this.getReader().getRemaining().trim().equals("");
    }

}
