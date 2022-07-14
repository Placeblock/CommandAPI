package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.TextComponent;

import java.util.*;
import java.util.function.Predicate;

public abstract class CommandNode<S> {
    @Getter
    private final String name;
    @Getter
    private final TextComponent description;
    @Getter
    private final List<String> permissions;

    private final Map<String, CommandNode<S>> children = new LinkedHashMap<>();
    private final Predicate<S> requirement;
    @Getter
    @Setter
    private Command<S> command;

    public CommandNode(String name, TextComponent description, List<String> permissions, Command<S> command, Predicate<S> requirement) {
        this.name = name;
        this.description = description;
        this.permissions = permissions;
        this.command = command;
        this.requirement = requirement;
    }

    public void addChild(final CommandNode<S> node) {
        final CommandNode<S> child = this.getChild(node.getName());
        if (child != null) {
            if (node.getCommand() != null) {
                child.setCommand(node.getCommand());
            }
            for (CommandNode<S> grandchild : child.getChildren()) {
                child.addChild(grandchild);
            }
        } else {
            this.children.put(node.getName(), node);
        }
    }

    public Collection<CommandNode<S>> getChildren() {
        return this.children.values();
    }

    public CommandNode<S> getChild(String name) {
        return this.children.get(name);
    }

    public boolean canUse(S source) {
        return this.requirement.test(source);
    }

    public abstract List<String> listSuggestions(CommandContext<S> context, String partial) throws CommandException;

    public abstract void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandException;

    public void print(int index) {
        System.out.println(" ".repeat(index * 5) + "Name: " + this.getName());
        System.out.println(" ".repeat(index * 5) + "Command: " + this.command);
        for (Map.Entry<String, CommandNode<S>> childEntry : this.children.entrySet()) {
            childEntry.getValue().print(index + 1);
        }
    }

    public List<List<CommandNode<S>>> getBranches() {
        List<List<CommandNode<S>>> branches = new ArrayList<>();
        List<CommandNode<S>> currentBranch = new ArrayList<>();
        currentBranch.add(this);
        branches.add(currentBranch);
        for (CommandNode<S> child : this.children.values()) {
            branches.addAll(child.getBranches().stream().peek(branch -> branch.add(0, this)).toList());
        }
        return branches;
    }

    public abstract TextComponent getUsageText();
}
