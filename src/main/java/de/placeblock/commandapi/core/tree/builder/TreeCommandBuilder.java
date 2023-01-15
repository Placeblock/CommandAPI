package de.placeblock.commandapi.core.tree.builder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.Parameter;
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
@Getter
@RequiredArgsConstructor
public abstract class TreeCommandBuilder<S> {

    private final String name;
    private final List<TreeCommandBuilder<S>> children = new ArrayList<>();
    private Consumer<ParseContext<S>> run = null;
    private TextComponent description;
    private String permission;

    public TreeCommandBuilder<S> literal(String name, Consumer<LiteralTreeCommandBuilder<S>> callback) {
        LiteralTreeCommandBuilder<S> literalTreeCommandBuilder = new LiteralTreeCommandBuilder<>(name);
        this.children.add(literalTreeCommandBuilder);
        callback.accept(literalTreeCommandBuilder);
        return this;
    }

    public <T> TreeCommandBuilder<S> parameter(String name, Parameter<S, T> parameter, Consumer<ParameterTreeCommandBuilder<S, T>> callback) {
        ParameterTreeCommandBuilder<S, T> parameterTreeCommandBuilder = new ParameterTreeCommandBuilder<>(name, parameter);
        this.children.add(parameterTreeCommandBuilder);
        callback.accept(parameterTreeCommandBuilder);
        return this;
    }

    public TreeCommandBuilder<S> run(Consumer<ParseContext<S>> callback) {
        this.run = callback;
        return this;
    }

    public TreeCommandBuilder<S> description(TextComponent description) {
        this.description = description;
        return this;
    }

    public TreeCommandBuilder<S> permission(String permission) {
        this.permission = permission;
        return this;
    }

    public abstract TreeCommand<S> build(Command<S> command);

}
