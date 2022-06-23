package de.placeblock.commandapi.builder;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.tree.RootCommandNode;
import lombok.Getter;

import java.util.Collection;
import java.util.function.Predicate;

public abstract class ArgumentBuilder<S, T extends ArgumentBuilder<S, T>> {
    private final RootCommandNode<S> arguments = new RootCommandNode<>();
    @Getter
    private Command<S> command;
    @Getter
    private Predicate<S> requirement = s -> true;

    protected abstract T getThis();

    public T then(final ArgumentBuilder<S, ?> argument) {
        arguments.addChild(argument.build());
        return getThis();
    }

    public T then(final CommandNode<S> argument) {
        arguments.addChild(argument);
        return getThis();
    }

    public T executes(final Command<S> command) {
        this.command = command;
        return getThis();
    }

    public Collection<CommandNode<S>> getArguments() {
        return arguments.getChildren();
    }

    public T requires(final Predicate<S> requirement) {
        this.requirement = requirement;
        return getThis();
    }

    public abstract CommandNode<S> build();


}
