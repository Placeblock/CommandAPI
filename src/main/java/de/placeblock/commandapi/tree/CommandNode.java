package de.placeblock.commandapi.tree;

import de.placeblock.commandapi.Command;
import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.suggestion.Suggestions;
import de.placeblock.commandapi.suggestion.SuggestionsBuilder;
import de.placeblock.commandapi.util.StringReader;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class CommandNode<S> {

    private final Map<String, CommandNode<S>> children = new LinkedHashMap<>();
    private final Map<String, LiteralCommandNode<S>> literals = new LinkedHashMap<>();
    private final Map<String, ArgumentCommandNode<S, ?>> arguments = new LinkedHashMap<>();
    private final Predicate<S> requirement;
    @Getter
    @Setter
    private Command<S> command;

    public CommandNode(Command<S> command, Predicate<S> requirement) {
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
            if (node instanceof LiteralCommandNode<S> literalCommandNode) {
                this.literals.put(node.getName(), literalCommandNode);
            } else if (node instanceof ArgumentCommandNode<S, ?> argumentCommandNode) {
                this.arguments.put(node.getName(), argumentCommandNode);
            }
        }
    }

    public Collection<? extends CommandNode<S>> getRelevantNodes(StringReader reader) {
        if (this.literals.size() > 0) {
            final int cursor = reader.getCursor();
            while (reader.canRead() && reader.peek() != ' ') {
                reader.skip();
            }
            String text = reader.getString().substring(cursor, reader.getCursor());
            reader.setCursor(cursor);
            LiteralCommandNode<S> literal = literals.get(text);
            if (literal != null) {
                return Collections.singleton(literal);
            } else {
                return arguments.values();
            }
        } else {
            return arguments.values();
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

    public abstract String getName();

    public abstract CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) throws CommandSyntaxException;

    public abstract void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException;

}
