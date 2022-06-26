package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.tree.CommandNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    private final List<CommandNode<S>> children = new ArrayList<>();
    @Getter
    private Command<S> command;
    @Getter
    private Predicate<S> requirement = s -> true;

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

    public Collection<CommandNode<S>> getChildren() {
        return children;
    }

    public T requires(final Predicate<S> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    public abstract CommandNode<S> build();


}
