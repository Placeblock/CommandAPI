package de.placeblock.commandapi.core;

import de.placeblock.commandapi.CommandAPI;
import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.context.ParseResults;
import de.placeblock.commandapi.core.context.ParsedArgument;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandNoPermissionException;
import de.placeblock.commandapi.core.exception.InvalidCommandException;
import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.tree.LiteralCommandNode;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;
import lombok.Getter;
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

    @Getter
    private final TextComponent prefix;

    public CommandAPICommand(String label) {
        super(label);
        this.commandNode = this.generateCommand().build();
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Registerd new Command:");
            this.commandNode.print(0);
        }
        this.prefix = Texts.subPrefix(Texts.primary(this.getName())).append(Component.space());
    }

    public abstract LiteralArgumentBuilder<S> generateCommand();
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean hasSourcePermission(S source, String permission);
    public abstract void sendSourceMessage(S source, TextComponent message);

    public void execute(S source, String input) {
        this.execute(source, new StringReader(input));
    }

    public void execute(S source, StringReader reader) {
        ParseResults<S> parse = this.parse(source, reader);
        this.execute(parse);
    }

    public void execute(ParseResults<S> parse) {
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Executing Parsed Command:");
            parse.getContext().print(0);
        }
        CommandContextBuilder<S> lastChild = parse.getContext().getLastChild();
        S source = lastChild.getSource();
        if (lastChild.getExceptions().size() > 0 ) {
            for (CommandException exception : lastChild.getExceptions()) {
                try {
                    throw exception;
                } catch (InvalidCommandException e) {
                    this.sendSourceMessage(source, this.generateHelpMessage(source));
                    return;
                } catch (CommandException e) {
                    this.sendSourceMessage(source, this.getPrefix().append(exception.getTextMessage()));
                }
            }
            return;
        }
        if (parse.getReader().canRead(2) || lastChild.getCommand() == null) {
            this.sendSourceMessage(source, this.generateHelpMessage(source));
            return;
        }
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Running Parsed Command:");
        }
        String command = parse.getReader().getString();
        lastChild.getCommand().run(lastChild.build(command));
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
            } catch (CommandException e) {
                contextSoFar.withException(e);
            }
        } else {
            contextSoFar.withException(new CommandNoPermissionException());
        }

        return parseResults;
    }



    public CompletableFuture<List<String>> getSuggestions(ParseResults<S> parse) {
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Getting Suggestions for Command:");
            parse.getContext().print(0);
        }
        CommandContextBuilder<S> context = parse.getContext();
        while (context.getChild() != null && context.getChild().getExceptions().size() == 0) {
            context = context.getChild();
        }
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Last Valid Child:");
            context.print(0);
        }
        String partial = parse.getReader().getRemaining();
        if (context.getExceptions().size() > 0 || (!partial.equals(" ") && partial.endsWith(" "))) {
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
            } catch (CommandException ignored) {
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
        TextComponent textComponent = Texts.headline(this.getName().toUpperCase());
        List<List<CommandNode<S>>> branches = this.commandNode.getBranches();
        branchLoop:
        for (List<CommandNode<S>> branch : branches) {
            CommandNode<S> lastNode = branch.get(branch.size() - 1);
            if (lastNode.getCommand() == null) {
                continue;
            }
            for (CommandNode<S> node : branch) {
                if (!node.canUse(source) || node.getPermissions().removeIf(permission -> !this.hasSourcePermission(source, permission))) {
                    continue branchLoop;
                }
            }
            textComponent = textComponent.append(Component.newline()).append(Texts.primary("/")).append(Texts.primary(branch.get(0).getName()));
            for (int i = 1; i < branch.size(); i++) {
                textComponent = textComponent.append(Component.space()).append(branch.get(i).getUsageText());
            }
            if (lastNode.getDescription() != null) {
                textComponent = textComponent.append(Component.newline()).append(lastNode.getDescription());
            }
        }
        return textComponent;
    }

}
