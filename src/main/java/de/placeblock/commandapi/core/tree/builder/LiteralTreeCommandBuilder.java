package de.placeblock.commandapi.core.tree.builder;

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
    TreeCommand<S> build() {
        return new LiteralTreeCommand<>(
            this.getName(),
            this.getChildren().stream().map(TreeCommandBuilder::build).toList(),
            this.getDescription(),
            this.getPermissions(),
            this.getRun()
        );
    }
}
