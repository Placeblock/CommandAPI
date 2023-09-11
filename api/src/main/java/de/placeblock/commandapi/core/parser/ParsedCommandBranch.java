package de.placeblock.commandapi.core.parser;

import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Placeblock
 */
@SuppressWarnings("unused")
@Getter
public class ParsedCommandBranch<S> extends ParameterHolder {
    private final StringReader reader;
    private final List<TreeCommand<S>> branch;
    private CommandParseException exception;

    public ParsedCommandBranch(StringReader reader) {
        super();
        this.reader = reader;
        this.branch = new ArrayList<>();
    }

    public ParsedCommandBranch(ParsedCommandBranch<S> parsedCommandBranch) {
        super(new HashMap<>(parsedCommandBranch.getParsedParameters()));
        this.reader = new StringReader(parsedCommandBranch.getReader());
        this.branch = new ArrayList<>(parsedCommandBranch.getBranch());
        this.exception = parsedCommandBranch.getException();
    }

    public TreeCommand<S> getLastParsedTreeCommand() {
        if (this.branch.size() == 0) return null;
        return this.branch.get(this.branch.size() - 1);
    }

    public void addParsedParameter(ParameterTreeCommand<?, ?> command, Object parameter) {
        this.parsedParameters.put(command, parameter);
    }


    public void setException(TreeCommand<S> command, CommandParseException exception) {
        this.exception = exception;
    }

}
