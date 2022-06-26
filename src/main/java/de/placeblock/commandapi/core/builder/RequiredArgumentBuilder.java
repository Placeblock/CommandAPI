package de.placeblock.commandapi.core.builder;

import de.placeblock.commandapi.core.arguments.ArgumentType;
import de.placeblock.commandapi.core.tree.ArgumentCommandNode;
import de.placeblock.commandapi.core.tree.CommandNode;
import lombok.Getter;
import java.util.List;
import java.util.function.Function;

@Getter
public class RequiredArgumentBuilder<S, T> extends ArgumentBuilder<S, RequiredArgumentBuilder<S, T>> {
    private final String name;
    private final ArgumentType<T> type;
    private Function<String, List<String>> customSuggestions = null;

    public RequiredArgumentBuilder(String name, ArgumentType<T> type) {
        super(name);
        this.name = name;
        this.type = type;
    }


    public RequiredArgumentBuilder<S, T> suggests(Function<String, List<String>> customSuggestions) {
        this.customSuggestions = customSuggestions;
        return getThis();
    }

    @Override
    protected RequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getName(), getDescription(), getPermissions(), getCommand(), getType(), getRequirement(), getCustomSuggestions());

        for (final CommandNode<S> argument : getChildren()) {
            result.addChild(argument);
        }

        return result;
    }
}
