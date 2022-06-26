package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.context.ParseResults;
import de.placeblock.commandapi.core.context.ParsedArgument;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.tree.ArgumentCommandNode;
import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.tree.LiteralCommandNode;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public abstract class CommandAPICommand<S> extends LiteralArgumentBuilder<S> {
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';

    private final LiteralCommandNode<S> commandNode;

    public CommandAPICommand(String label) {
        super(label);
        this.commandNode = this.generateCommand().build();
        System.out.println("CREATED NEW COMMAND:");
        this.commandNode.print(0);
    }

    public abstract LiteralArgumentBuilder<S> generateCommand();
    public abstract boolean hasSourcePermission(S source, String permission);
    protected abstract void sendSourceMessage(S source, TextComponent message);

    public void execute(S source, String input) throws CommandSyntaxException {
        this.execute(source, new StringReader(input));
    }

    public void execute(S source, StringReader reader) throws CommandSyntaxException {
        ParseResults<S> parse = this.parse(source, reader);
        this.execute(parse);
    }

    public void execute(ParseResults<S> parse) throws CommandSyntaxException {
        parse.getContext().print(0);
        System.out.println(parse.getReader().debugString());

        CommandContextBuilder<S> commandContextBuilder = parse.getContext().getLastChild();

        if (parse.getReader().canRead(2)) {
            if (commandContextBuilder.getExceptions().size() == 1) {
                throw commandContextBuilder.getExceptions().iterator().next();
            } else if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create();
            } else {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
            }
        }

        String command = parse.getReader().getString();
        CommandContext<S> context = commandContextBuilder.build(command);
        if (context.getCommand() != null) {
            context.getCommand().run(context);
        } else {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().create();
        }
    }

    public ParseResults<S> parse(S source, String command) {
        return this.parse(source, new StringReader(command));
    }

    public ParseResults<S> parse(S source, StringReader reader) {
        return this.parseNodes(this.commandNode, reader, new CommandContextBuilder<>(source, reader.getCursor()));
    }

    public ParseResults<S> parseNodes(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar) {
        S source = contextSoFar.getSource();
        ParseResults<S> parseResults = new ParseResults<>(contextSoFar, originalReader);

        if (node.canUse(source) && !node.getPermissions().removeIf(permission -> !this.hasSourcePermission(source, permission))) {
            try {
                try {
                    node.parse(originalReader, contextSoFar);
                    contextSoFar.withCommand(node.getCommand());
                    if (originalReader.canRead(2)) {
                        originalReader.skip();
                        for (CommandNode<S> child : node.getChildren()) {
                            CommandContextBuilder<S> childCommandContext = new CommandContextBuilder<>(source, originalReader.getCursor());
                            for (Map.Entry<String, ParsedArgument<S, ?>> argumentEntry : contextSoFar.getArguments().entrySet()) {
                                childCommandContext.withArgument(argumentEntry.getKey(), argumentEntry.getValue());
                            }
                            ParseResults<S> parse = this.parseNodes(child, originalReader, childCommandContext);
                            contextSoFar.withChild(parse.getContext());
                        }
                    }
                } catch (CommandSyntaxException e) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create(e.getRawMessage());
                }
            } catch (CommandSyntaxException e) {
                contextSoFar.withException(e);
            }
        } else {
            contextSoFar.withException(CommandSyntaxException.BUILT_IN_EXCEPTIONS.nopermissionException().create());
        }

        return parseResults;
    }



    public CompletableFuture<List<String>> getSuggestions(ParseResults<S> parse) {
        CommandContextBuilder<S> context = parse.getContext();
        while (context.getChild() != null && context.getChild().getExceptions().size() == 0) {
            context = context.getChild();
        }
        String partial = parse.getReader().getRemaining();
        if ("".equals(partial) || context.getExceptions().size() > 0) {
            return CompletableFuture.completedFuture(new ArrayList<>());
        }
        partial = partial.strip();
        Collection<CommandNode<S>> children = context.getNode().getChildren();
        @SuppressWarnings("unchecked") CompletableFuture<List<String>>[] futures = new CompletableFuture[children.size()];
        int i = 0;
        for (CommandNode<S> child : children) {
            CompletableFuture<List<String>> future = CompletableFuture.completedFuture(new ArrayList<>());
            try {
                future = child.listSuggestions(context.build(parse.getReader().getString().substring(0, parse.getReader().getCursor())), partial);
            } catch (CommandSyntaxException ignored) {
            }
            futures[i++] = future;
        }
        CompletableFuture<List<String>> result = new CompletableFuture<>();
        CompletableFuture.allOf(futures).thenRun(() -> {
            final List<String> suggestions = new ArrayList<>();
            for (CompletableFuture<List<String>> future : futures) {
                suggestions.addAll(future.join());
            }
            result.complete(suggestions);
        });

        return result;
    }

    public TextComponent generateHelpMessage(S source) {
        TextComponent textComponent = Texts.headline(this.getName().toUpperCase()).append(Component.newline());
        List<List<CommandNode<S>>> branches = this.commandNode.getBranches();
        for (List<CommandNode<S>> branch : branches) {
            if (branch.get(branch.size() - 1).getCommand() == null) {
                continue;
            }
            for (CommandNode<S> node : branch) {
                if (!node.canUse(source) || node.getPermissions().removeIf(permission -> !this.hasSourcePermission(source, permission))) {
                    continue;
                }
            }
            textComponent = textComponent.append(Texts.primary("/")).append(Texts.primary(branch.get(0).getName()));
            for (int i = 1; i < branch.size(); i++) {
                CommandNode<S> node = branch.get(i);
                if (node instanceof LiteralCommandNode<S>) {
                    textComponent = textComponent.append(Component.space()).append(node.getUsageText());
                } else if (node instanceof ArgumentCommandNode<S,?>) {
                    if (branch.get(i - 1).getCommand() == null) {
                        textComponent = textComponent.append(Component.space()).append(node.getUsageText());
                    } else {
                        textComponent = textComponent.append(Component.space()).append(Texts.secondary("[")).append(node.getUsageText()).append(Texts.secondary("]"));
                    }
                }
            }
        }
        return textComponent;
    }

}
