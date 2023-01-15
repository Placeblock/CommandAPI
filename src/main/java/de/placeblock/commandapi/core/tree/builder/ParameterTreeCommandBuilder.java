package de.placeblock.commandapi.core.tree.builder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommandBuilder<S, T> extends TreeCommandBuilder<S> {

    private final Parameter<S, T> parameter;

    public ParameterTreeCommandBuilder(String name, Parameter<S, T> parameter) {
        super(name);
        this.parameter = parameter;
    }

    @Override
    public TreeCommand<S> build(Command<S> command) {
        return new ParameterTreeCommand<>(
            command,
            this.getName(),
            this.getChildren().stream().map(treeCommand -> treeCommand.build(command)).toList(),
            this.getDescription(),
            this.getPermission(),
            this.getRun(),
            this.getParameter()
        );
    }

}
