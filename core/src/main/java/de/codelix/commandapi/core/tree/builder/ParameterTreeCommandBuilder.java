package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.ParameterTreeCommand;
import de.codelix.commandapi.core.tree.TreeCommand;
import lombok.Getter;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterTreeCommandBuilder<S, T> extends TreeCommandBuilder<S, ParameterTreeCommandBuilder<S, T>> {

    private final Parameter<S, T> parameter;

    public ParameterTreeCommandBuilder(String name, Parameter<S, T> parameter) {
        super(name);
        this.parameter = parameter;
    }

    @Override
    protected ParameterTreeCommandBuilder<S, T> getThis() {
        return this;
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
