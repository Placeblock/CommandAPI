package de.placeblock.commandapi;

import de.placeblock.commandapi.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.context.ParseResults;
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
        this.execute(source, new StringReader(input.strip()));
    }

    public void execute(S source, StringReader reader) throws CommandSyntaxException {
        ParseResults<S> parse = this.parse(source, reader);
        this.execute(parse);
    }

    public void execute(ParseResults<S> parse) throws CommandSyntaxException {
        parse.getContext().print(0);

        if (parse.getReader().canRead()) {
            if (parse.getExceptions().size() == 1) {
                throw parse.getExceptions().values().iterator().next();
            } else if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            } else {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
            }
        }

        String command = parse.getReader().getString();
        CommandContext<S> context = parse.getContext().build(command);
        if (context.getCommand() != null) {
            context.getCommand().run(context);
        } else {
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
        }
    }

    public ParseResults<S> parse(S source, String command) {
        return this.parse(source, new StringReader(command.strip()));
    }

    public ParseResults<S> parse(S source, StringReader reader) {
        return this.parseNodes(this.commandNode, reader, new CommandContextBuilder<>(source, reader.getCursor()));
    }

    public ParseResults<S> parseNodes(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar) {
        System.out.println("Completions String 1 ("+node.getName()+"):" + originalReader.getString().substring(0, originalReader.getCursor()) + "|" + originalReader.getString().substring(originalReader.getCursor()));

        S source = contextSoFar.getSource();
        Map<CommandNode<S>, CommandSyntaxException> errors = new HashMap<>();
        List<ParseResults<S>> potentials = new ArrayList<>();

        if (node.canUse(source)) {
            try {
                try {
                    node.parse(originalReader, contextSoFar);
                    System.out.println("Completions String 2:" + originalReader.getString().substring(0, originalReader.getCursor()) + "|" + originalReader.getString().substring(originalReader.getCursor()));
                    contextSoFar.withCommand(node.getCommand());
                    if (originalReader.canRead(2)) {
                        originalReader.skip();
                        for (CommandNode<S> child : node.getChildren()) {
                            ParseResults<S> parse = this.parseNodes(child, originalReader, new CommandContextBuilder<>(source, originalReader.getCursor()));
                            System.out.println("Completions String 3 ("+child.getName()+"):" + originalReader.getString().substring(0, originalReader.getCursor()) + "|" + originalReader.getString().substring(parse.getReader().getCursor()));
                            System.out.println("Exceptions: " + parse.getExceptions());
                            if (parse.getExceptions().size() == 0) {
                                System.out.println("ADDING "+parse.getContext().getNode().getName()+" TO POTENTIALS");
                                potentials.add(parse);
                            }
                        }
                    } else {
                        potentials.add(new ParseResults<>(contextSoFar, originalReader, errors));
                    }
                } catch (CommandSyntaxException e) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(originalReader, e.getMessage());
                }
                if (originalReader.canRead() && originalReader.peek() != ARGUMENT_SEPARATOR_CHAR) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(originalReader);
                }
            } catch (CommandSyntaxException e) {
                errors.put(node, e);
            }

        }
        if (potentials.size() > 0) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return potentials.get(0);
        }
        System.out.println("Completions String 4:" + originalReader.getString().substring(0, originalReader.getCursor()) + "|" + originalReader.getString().substring(originalReader.getCursor()));

        return new ParseResults<>(contextSoFar, originalReader, errors);
    }

    public CompletableFuture<List<String>> getSuggestions(ParseResults<S> parse) {
        final CommandContextBuilder<S> context = parse.getContext();

        Collection<CommandNode<S>> children = context.getNode().getChildren();
        @SuppressWarnings("unchecked") CompletableFuture<List<String>>[] futures = new CompletableFuture[children.size()];
        int i = 0;
        for (CommandNode<S> child : children) {
            CompletableFuture<List<String>> future = CompletableFuture.completedFuture(new ArrayList<>());
            String partial = parse.getReader().getRemaining();
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
