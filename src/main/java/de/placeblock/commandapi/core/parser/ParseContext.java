package de.placeblock.commandapi.core.parser;

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
    private final Map<String, ParsedValue<?>> parameters = new HashMap<>();
    @Setter
    private boolean noPermission = true;
    @Setter
    private List<TreeCommand<S>> parsedCommands = new ArrayList<>();

    public ParseContext(String text, S source) {
        this(new StringReader(text), source);
    }

    public TreeCommand<S> getLastParsedCommand() {
        if (this.parsedCommands.size() == 0) return null;
        return this.parsedCommands.get(this.parsedCommands.size() - 1);
    }

    public void addParameter(String name, ParsedValue<?> parameter) {
        this.parameters.put(name, parameter);
    }

    public void addParsedCommand(TreeCommand<S> command) {this.parsedCommands.add(command);}

    public ParsedValue<?> getParameter(String name) {
        return this.parameters.get(name);
    }

    public <T> ParsedValue<T> getParameter(String name, Class<T> type) {
        //noinspection unchecked
        return (ParsedValue<T>) this.parameters.get(name);
    }

    public boolean isNotParsedToEnd() {
        return !this.getReader().getRemaining().trim().equals("");
    }

}
