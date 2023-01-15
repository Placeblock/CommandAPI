package de.placeblock.commandapi.core.tree.builder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;

/**
 * Author: Placeblock
 */
@Getter
public class LiteralTreeCommandBuilder<S> extends TreeCommandBuilder<S> {

    public LiteralTreeCommandBuilder(String name) {
        super(name);
    }

    @Override
    public TreeCommand<S> build(Command<S> command) {
        return new LiteralTreeCommand<>(
            command,
            this.getName(),
            this.getChildren().stream().map(treeCommand -> treeCommand.build(command)).toList(),
            this.getDescription(),
            this.getPermission(),
            this.getRun()
        );
    }
}
