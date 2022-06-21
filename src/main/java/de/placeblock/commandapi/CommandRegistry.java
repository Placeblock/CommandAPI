package de.placeblock.commandapi;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.context.CommandContextBuilder;
import de.placeblock.commandapi.context.ParseResults;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.tree.CommandNode;
import de.placeblock.commandapi.tree.LiteralCommandNode;
import de.placeblock.commandapi.tree.RootCommandNode;
import de.placeblock.commandapi.util.StringReader;

import java.util.*;

@SuppressWarnings("unused")
public class CommandRegistry<S> {

    private final RootCommandNode<S> rootCommandNode;

    public CommandRegistry() {
        this.rootCommandNode = new RootCommandNode<>();
    }

    public void register(LiteralCommandNode<S> literalCommandNode) {
        this.rootCommandNode.addChild(literalCommandNode);
    }

    public void execute(S source, String input) throws CommandSyntaxException {
        this.execute(source, new StringReader(input));
    }

    public void execute(S source, StringReader reader) throws CommandSyntaxException {
        ParseResults<S> parse = this.parse(source, reader);
        this.execute(parse);
    }

    public void execute(ParseResults<S> parse) throws CommandSyntaxException {
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
        CommandContext<S> original = parse.getContext().build(command);
        List<CommandContext<S>> contexts = Collections.singletonList(original);
        ArrayList<CommandContext<S>> next = null;

        while (contexts != null) {
            for (CommandContext<S> context : contexts) {
                CommandContext<S> child = context.getChild();
                if (child != null) {
                    if (child.hasNodes()) {
                        if (next == null) {
                            next = new ArrayList<>(1);
                        }
                        next.add(child);
                    }
                } else if (context.getCommand() != null) {
                    context.getCommand().run(context);
                }
            }
            contexts = next;
            next = null;
        }
    }

    public ParseResults<S> parse(S source, String command) {
        return this.parse(source, new StringReader(command));
    }

    public ParseResults<S> parse(S source, StringReader reader) {
        return this.parseNodes(this.rootCommandNode, reader, new CommandContextBuilder<>(source));
    }

    public ParseResults<S> parseNodes(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar) {
        final S source = contextSoFar.getSource();
        Map<CommandNode<S>, CommandSyntaxException> errors = null;
        List<ParseResults<S>> potentials = null;
        final int cursor = originalReader.getCursor();

        for (CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue;
            }
            CommandContextBuilder<S> context = contextSoFar.copy();
            StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                } catch (RuntimeException ex) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                }
                if (reader.canRead()) {
                    if (reader.canRead()) {
                        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                    }
                }
            } catch (CommandSyntaxException ex) {
                if (errors == null) {
                    errors = new LinkedHashMap<>();
                }
                errors.put(child, ex);
                reader.setCursor(cursor);
                continue;
            }

            context.withCommand(child.getCommand());
            if (reader.canRead(2)) {
                reader.skip();
                ParseResults<S> parse = parseNodes(child, reader, context);
                if (potentials == null) {
                    potentials = new ArrayList<>(1);
                }
                potentials.add(parse);
            } else {
                if (potentials == null) {
                    potentials = new ArrayList<>(1);
                }
                potentials.add(new ParseResults<>(context, reader, Collections.emptyMap()));
            }
        }

        if (potentials != null) {
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

        return new ParseResults<>(contextSoFar, originalReader, errors == null ? Collections.emptyMap() : errors);
    }
}
