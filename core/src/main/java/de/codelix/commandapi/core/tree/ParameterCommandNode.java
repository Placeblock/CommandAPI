package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.CommandExecutor;
import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterCommandNode<S, T> extends CommandNode<S> {
    private final Parameter<S, T> parameter;

    public ParameterCommandNode(Command<S> command, String name, List<CommandNode<S>> children, TextComponent description,
                                String permission, CommandExecutor<S> run, Parameter<S, T> parameter) {
        super(command, name, children, description, permission, run);
        this.parameter = parameter;
    }

    @Override
    protected void parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        command.getBranch().add(this);
        T result = this.parameter.parse(command, source);
        command.addParsedParameter(this, result);
    }

    @Override
    public List<String> getSuggestions(ParsedCommandBranch<S> command, S source) {
        // If there is an error we only want to send an empty arraylist if we are not at the beginning of the parameter:
        // test command |012
        // This fails because it cannot parse an empty string to an integer.
        if (this.hasNoPermission(source)) {
            return new ArrayList<>();
        }
        SuggestionBuilder<S> suggestionBuilder = new SuggestionBuilder<>(this, command.getReader().getRemaining(), source, new HashMap<>(command.getParsedParameters()));
        this.parameter.getSuggestions(suggestionBuilder);
        return suggestionBuilder.getSuggestions();
    }
}
