package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.tree.LiteralTreeCommand;
import de.codelix.commandapi.core.tree.TreeCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
public class LiteralTreeCommandBuilder<S> extends TreeCommandBuilder<S, LiteralTreeCommandBuilder<S>> {
    private final List<String> aliases = new ArrayList<>();

    public LiteralTreeCommandBuilder(String name) {
        super(name);
    }

    @Override
    protected LiteralTreeCommandBuilder<S> getThis() {
        return this;
    }

    @SuppressWarnings("unused")
    public LiteralTreeCommandBuilder<S> withAlias(String alias) {
        this.aliases.add(alias);
        return this.getThis();
    }

    @Override
    public TreeCommand<S> build(Command<S> command) {
        return new LiteralTreeCommand<>(
            command,
            this.getName(),
            this.getChildren().stream().map(treeCommand -> treeCommand.build(command)).toList(),
            this.getDescription(),
            this.getPermission(),
            this.getRun(),
            this.aliases
        );
    }
}