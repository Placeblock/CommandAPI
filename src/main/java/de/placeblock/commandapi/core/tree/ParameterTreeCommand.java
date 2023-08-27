package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.CommandExecutor;
import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.parser.ParsedCommandBranch;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommand<S, T> extends TreeCommand<S> {
    private final Parameter<S, T> parameter;

    public ParameterTreeCommand(Command<S> command, String name, List<TreeCommand<S>> children, TextComponent description,
                                String permission, CommandExecutor<S> run, Parameter<S, T> parameter) {
        super(command, name, children, description, permission, run);
        this.parameter = parameter;
    }

    @Override
    protected void parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        command.getBranch().add(this);
        T result = this.parameter.parse(command, source);
        command.addParsedParameter(this.getName(), result);
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

    @Override
    public TextComponent getHelpComponent() {
        return Texts.inferior("[" + this.getName() + "]");
    }

    @Override
    public TextComponent getHelpExtraDescription() {
        return null;
    }
}
