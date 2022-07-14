package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.tree.CommandNode;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter
@SuppressWarnings("unused")
public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    private final String name;
    private TextComponent description;
    private final List<String> permissions = new ArrayList<>();
    private final List<CommandNode<S>> children = new ArrayList<>();
    private Command<S> command;
    private Predicate<S> requirement = s -> true;

    protected ArgumentBuilder(String name) {
        this.name = name;
    }

    protected abstract T getThis();

    public T then(final ArgumentBuilder<S, ?> argument) {
        return this.then(argument.build());
    }

    public T then(final CommandNode<S> argument) {
        children.add(argument);
        return getThis();
    }

    public T executes(final Command<S> command) {
        this.command = command;
        return getThis();
    }

    public T withDescription(TextComponent description) {
        this.description = description;
        return getThis();
    }

    public T withPermission(String permission) {
        this.permissions.add(permission);
        return getThis();
    }

    public T requires(final Predicate<S> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    public abstract CommandNode<S> build();


}
