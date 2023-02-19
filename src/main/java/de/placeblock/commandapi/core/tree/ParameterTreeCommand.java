package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommand<S, T> extends TreeCommand<S> {
    private final Parameter<S, T> parameter;

    public ParameterTreeCommand(Command<S> command, String name, List<TreeCommand<S>> children, TextComponent description,
                                String permission, Consumer<ParseContext<S>> run, Parameter<S, T> parameter) {
        super(command, name, children, description, permission, run);
        this.parameter = parameter;
    }

    @Override
    boolean parse(ParseContext<S> context) {
        ParsedValue<?> result = this.parameter.parse(context, this);
        context.addParameter(this.getName(), result);
        return !result.isInvalid();
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        // If there is an error we only want to send an empty arraylist if we are not at the beginning of the parameter:
        // test command |012
        // This fails because it cannot parse an empty string to an integer.
        if (this.hasNoPermission(context.getSource())) {
            return new ArrayList<>();
        }
        return this.parameter.getSuggestions(context, this);
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
