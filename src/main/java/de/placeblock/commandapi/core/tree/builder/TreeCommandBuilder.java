package de.placeblock.commandapi.core.tree.builder;

import de.placeblock.commandapi.core.parameter.Parameter;
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
@Getter
@RequiredArgsConstructor
public abstract class TreeCommandBuilder<S> {
    S source;

    private final String name;
    private final List<TreeCommandBuilder<S>> children = new ArrayList<>();
    private Consumer<S> run = null;
    private TextComponent description;
    private List<String> permissions = new ArrayList<>();

    public TreeCommandBuilder<S> literal(String name, Consumer<LiteralTreeCommandBuilder<S>> callback) {
        LiteralTreeCommandBuilder<S> literalTreeCommandBuilder = new LiteralTreeCommandBuilder<>(name);
        this.children.add(literalTreeCommandBuilder);
        callback.accept(literalTreeCommandBuilder);
        return this;
    }

    public <T> TreeCommandBuilder<S> parameter(String name, Parameter<T> parameter, Consumer<ParameterTreeCommandBuilder<S, T>> callback) {
        ParameterTreeCommandBuilder<S, T> parameterTreeCommandBuilder = new ParameterTreeCommandBuilder<>(name, parameter);
        this.children.add(parameterTreeCommandBuilder);
        callback.accept(parameterTreeCommandBuilder);
        return this;
    }

    public TreeCommandBuilder<S> run(Consumer<S> callback) {
        this.run = callback;
        return this;
    }

    public TreeCommandBuilder<S> description(TextComponent description) {
        this.description = description;
        return this;
    }

    public TreeCommandBuilder<S> permission(String permission) {
        this.permissions.add(permission);
        return this;
    }

    abstract TreeCommand<S> build();

}
