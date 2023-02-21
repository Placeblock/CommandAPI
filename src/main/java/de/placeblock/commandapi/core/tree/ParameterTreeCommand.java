package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.CommandExecutor;
import de.placeblock.commandapi.core.SuggestionBuilder;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.parser.StringReader;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
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
    protected void parse(ParsedCommand<S> command, S source) throws CommandSyntaxException {
        command.getParsedTreeCommands().add(this);
        StringReader reader = command.getReader();
        StringReader readerCopy = new StringReader(reader);
        try {
            T result = this.parameter.parse(command);
            command.addParsedParameter(this.getName(), result);
        } finally {
            command.getParsedTreeCommandStrings().put(this, reader.getString().substring(readerCopy.getCursor(), reader.getCursor()));
        }
    }

    @Override
    public List<String> getSuggestions(ParsedCommand<S> command, S source) {
        // If there is an error we only want to send an empty arraylist if we are not at the beginning of the parameter:
        // test command |012
        // This fails because it cannot parse an empty string to an integer.
        if (this.hasNoPermission(source)) {
            return new ArrayList<>();
        }
        return this.parameter.getSuggestions(new SuggestionBuilder<>(this, command.getReader().getRemaining(), source));
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
