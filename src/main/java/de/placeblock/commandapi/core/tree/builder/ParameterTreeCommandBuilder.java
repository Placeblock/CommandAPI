package de.placeblock.commandapi.core.tree.builder;

import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommandBuilder<S, T> extends TreeCommandBuilder<S> {

    private final Parameter<T> parameter;

    public ParameterTreeCommandBuilder(String name, Parameter<T> parameter) {
        super(name);
        this.parameter = parameter;
    }

    @Override
    TreeCommand<S> build() {
        return new ParameterTreeCommand<>(
            this.getName(),
            this.getChildren().stream().map(TreeCommandBuilder::build).toList(),
            this.getDescription(),
            this.getPermissions(),
            this.getRun(),
            this.getParameter()
        );
    }

}
