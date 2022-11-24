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
    private final ArgumentType<S, T> type;
    private Function<String, List<String>> customSuggestions = null;

    protected RequiredArgumentBuilder(String name, ArgumentType<S, T> type) {
        super(name);
        this.name = name;
        this.type = type;
    }

    @SuppressWarnings("unused")
    public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<S, T> type) {
        return new RequiredArgumentBuilder<>(name, type);
    }

    @SuppressWarnings("unused")
    public RequiredArgumentBuilder<S, T> suggests(Function<String, List<String>> customSuggestions) {
        this.customSuggestions = customSuggestions;
        return getThis();
    }

    @Override
    protected RequiredArgumentBuilder<S, T> getThis() {
        return this;
    }

    public ArgumentCommandNode<S, T> build() {
        final ArgumentCommandNode<S, T> result = new ArgumentCommandNode<>(getName(), getDescription(), getPermissions(), getCommand(), getType(), getRequirement(), getCustomSuggestions(), isAsync(), isRecursiveAsync());

        for (final CommandNode<S> argument : getChildren()) {
            result.addChild(argument);
        }

        return result;
    }
}
