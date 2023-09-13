package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.tree.LiteralCommandNode;
import de.codelix.commandapi.core.tree.CommandNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
public class LiteralCommandNodeBuilder<S> extends CommandNodeBuilder<S, LiteralCommandNodeBuilder<S>> {
    private final List<String> aliases = new ArrayList<>();

    public LiteralCommandNodeBuilder(String name) {
        super(name);
    }

    @Override
    protected LiteralCommandNodeBuilder<S> getThis() {
        return this;
    }

    @SuppressWarnings("unused")
    public LiteralCommandNodeBuilder<S> withAlias(String alias) {
        this.aliases.add(alias);
        return this.getThis();
    }

    @Override
    public CommandNode<S> build(Command<S> command) {
        return new LiteralCommandNode<>(
            command,
            this.getName(),
            this.getDisplayName(),
            this.getChildren().stream().map(treeCommand -> treeCommand.build(command)).toList(),
            this.getDescription(),
            this.getPermission(),
            this.getRun(),
            this.aliases
        );
    }
}
