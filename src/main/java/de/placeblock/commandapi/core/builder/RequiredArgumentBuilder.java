package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.core.arguments.ArgumentType;
import de.placeblock.commandapi.core.tree.ArgumentCommandNode;
import de.placeblock.commandapi.core.tree.CommandNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {
    private final String name;
    private final ArgumentType<T> type;
    private Function<String, CompletableFuture<List<String>>> customSuggestions = null;


    public RequiredArgumentBuilder<S, T> suggests(Function<String, CompletableFuture<List<String>>> customSuggestions) {
        this.customSuggestions = customSuggestions;
        return getThis();
    }

    @Override
    protected RequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getCommand(), getName(), getType(), getRequirement(), getCustomSuggestions());

        for (final CommandNode<S> argument : getChildren()) {
            result.addChild(argument);
        }

        return result;
    }
}
