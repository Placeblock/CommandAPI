package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.parser.ParseContext;
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
        T result = this.parameter.parse(context, this);
        if (result != null) {
            context.addParameter(this.getName(), result);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        if (context.getErrors().containsKey(this) || this.hasNoPermission(context.getSource())) {
            return new ArrayList<>();
        }
        return this.parameter.getSuggestions(context, this);
    }

    @Override
    public TextComponent getHelpComponent() {
        return Texts.inferior("[" + this.getName() + "]");
    }
}
