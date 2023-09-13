package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.ParameterCommandNode;
import de.codelix.commandapi.core.tree.CommandNode;
import lombok.Getter;

/**
 * Author: Placeblock
 */
@Getter
public class ParameterCommandNodeBuilder<S, T> extends CommandNodeBuilder<S, ParameterCommandNodeBuilder<S, T>> {

    private final Parameter<S, T> parameter;

    public ParameterCommandNodeBuilder(String name, Parameter<S, T> parameter) {
        super(name);
        this.parameter = parameter;
    }

    @Override
    protected ParameterCommandNodeBuilder<S, T> getThis() {
        return this;
    }

    @Override
    public CommandNode<S> build(Command<S> command) {
        return new ParameterCommandNode<>(
            command,
            this.getName(),
            this.getDisplayName(),
            this.getChildren().stream().map(treeCommand -> treeCommand.build(command)).toList(),
            this.getDescription(),
            this.getPermission(),
            this.getRun(),
            this.getParameter()
        );
    }

}
