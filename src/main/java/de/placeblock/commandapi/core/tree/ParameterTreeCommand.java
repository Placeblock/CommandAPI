package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommand<S, T> extends TreeCommand<S> {
    private final Parameter<S, T> parameter;

    public ParameterTreeCommand(String name, List<TreeCommand<S>> children, TextComponent description,
                                List<String> permissions, Consumer<S> run, Parameter<S, T> parameter) {
        super(name, children, description, permissions, run);
        this.parameter = parameter;
    }

    @Override
    void parse(ParseContext<S> context) {
        T result = this.parameter.parse(context);
        if (result != null) {
            context.addParameter(this.getName(), result);
            context.setLastParsedCommand(this);
        }
    }

    @Override
    public List<String> getSuggestions(ParseContext<S> context) {
        return this.parameter.getSuggestions(context);
    }
}
