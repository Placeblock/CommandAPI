package de.codelix.commandapi.core.tree.builder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.CommandExecutor;
import de.codelix.commandapi.core.tree.CommandNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@SuppressWarnings("ALL")
@Getter
@RequiredArgsConstructor
public abstract class CommandNodeBuilder<S, CT extends CommandNodeBuilder<S, CT>> {

    private final String name;
    private final List<CommandNodeBuilder<S, ?>> children = new ArrayList<>();
    private CommandExecutor<S> run = null;
    private TextComponent description;
    private String permission;

    protected abstract CT getThis();

    public CT then(CommandNodeBuilder<S, ?> builder) {
        this.children.add(builder);
        return getThis();
    }

    public CT run(CommandExecutor<S> callback) {
        this.run = callback;
        return getThis();
    }

    public CT withDescription(TextComponent description) {
        this.description = description;
        return getThis();
    }

    public CT withPermission(String permission) {
        this.permission = permission;
        return getThis();
    }

    public abstract CommandNode<S> build(Command<S> command);

}
