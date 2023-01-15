package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.parameter.Parameter;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommand<S, T> extends TreeCommand<S> {
    private final Parameter<T> parameter;

    public ParameterTreeCommand(String name, List<TreeCommand<S>> children, TextComponent description,
                                List<String> permissions, Consumer<S> run, Parameter<T> parameter) {
        super(name, children, description, permissions, run);
        this.parameter = parameter;
    }
}
