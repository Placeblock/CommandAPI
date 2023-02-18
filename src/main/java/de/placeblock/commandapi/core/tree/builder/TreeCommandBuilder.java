package de.placeblock.commandapi.core.tree.builder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Author: Placeblock
 */
@SuppressWarnings("ALL")
@Getter
@RequiredArgsConstructor
public abstract class TreeCommandBuilder<S, CT extends TreeCommandBuilder<S, CT>> {

    private final String name;
    private final List<TreeCommandBuilder<S, ?>> children = new ArrayList<>();
    private Consumer<ParseContext<S>> run = null;
    private TextComponent description;
    private String permission;

    protected abstract CT getThis();

    public CT then(TreeCommandBuilder<S, ?> builder) {
        this.children.add(builder);
        return getThis();
    }

    public CT run(Consumer<ParseContext<S>> callback) {
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

    public abstract TreeCommand<S> build(Command<S> command);

}
