package de.placeblock.commandapi;

import de.placeblock.commandapi.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.context.ParseResults;
import de.placeblock.commandapi.context.ParsedArgument;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.tree.LiteralCommandNode;
import de.placeblock.commandapi.util.StringReader;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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

        if (node.canUse(source)) {
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

}
