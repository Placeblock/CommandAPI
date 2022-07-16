package de.placeblock.commandapi.core;

import de.placeblock.commandapi.CommandAPI;
import de.placeblock.commandapi.core.arguments.StringArgumentType;
import de.placeblock.commandapi.core.arguments.StringType;
import de.placeblock.commandapi.core.builder.LiteralArgumentBuilder;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.context.ParseResults;
import de.placeblock.commandapi.core.context.ParsedArgument;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.CommandNoPermissionException;
import de.placeblock.commandapi.core.exception.InvalidCommandException;
import de.placeblock.commandapi.core.tree.ArgumentCommandNode;
import de.placeblock.commandapi.core.tree.CommandNode;
import de.placeblock.commandapi.core.tree.LiteralCommandNode;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Trying to Parse Node " + node.getName());
        }
        S source = contextSoFar.getSource();
        ParseResults<S> parseResults = new ParseResults<>(contextSoFar, originalReader);

        if (node.canUse(source) && node.getPermissions().stream().filter(permission -> !this.hasSourcePermission(source, permission)).toList().size() == 0) {
            if (CommandAPI.DEBUG_MODE) {
                System.out.println("has permission!");
            }
            try {
                if (CommandAPI.DEBUG_MODE) {
                    System.out.println("StringReader: " + originalReader.debugString());
                }
                node.parse(originalReader, contextSoFar);
                if (CommandAPI.DEBUG_MODE) {
                    System.out.println("After String Reader: " + originalReader.debugString());
                }
                contextSoFar.withCommand(node.getCommand());
                if (originalReader.canRead(2)) {
                    if (CommandAPI.DEBUG_MODE) {
                        System.out.println("Can read further");
                    }
                    originalReader.skip();
                    List<ParseResults<S>> potentials = new ArrayList<>();
                    for (CommandNode<S> child : node.getChildren()) {
                        if (CommandAPI.DEBUG_MODE) {
                            System.out.println("parsing child " + child.getName());
                        }
                        CommandContextBuilder<S> childCommandContext = new CommandContextBuilder<>(source, originalReader.getCursor());
                        for (Map.Entry<String, ParsedArgument<S, ?>> argumentEntry : contextSoFar.getArguments().entrySet()) {
                            childCommandContext.withArgument(argumentEntry.getKey(), argumentEntry.getValue());
                        }
                        ParseResults<S> parse = this.parseNodes(child, new StringReader(originalReader), childCommandContext);
                        if (CommandAPI.DEBUG_MODE) {
                            System.out.println("Context: ");
                            parse.getContext().print(0);
                            System.out.println("After Child String Reader: " + originalReader.getCursor());
                        }
                        potentials.add(parse);
                    }
                    if (potentials.size() > 1) {
                        potentials.sort((a, b) -> {
                            if (!a.getReader().canRead() && b.getReader().canRead()) {
                                return -1;
                            }
                            if (a.getReader().canRead() && !b.getReader().canRead()) {
                                return 1;
                            }
                            CommandContextBuilder<S> childA = a.getContext();
                            CommandContextBuilder<S> childB = b.getContext();
                            if (childA.getExceptions().isEmpty() && !childB.getExceptions().isEmpty()) {
                                return -1;
                            }
                            if (!childA.getExceptions().isEmpty() && childB.getExceptions().isEmpty()) {
                                return 1;
                            }
                            return 0;
                        });
                    }
                    if (potentials.size() > 0) {
                        originalReader.setCursor(potentials.get(0).getReader().getCursor());
                        contextSoFar.withChild(potentials.get(0).getContext());
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



    public List<String> getSuggestions(ParseResults<S> parse) {
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Getting Suggestions for Command: " + parse.getReader().debugString());
            parse.getContext().print(0);
            System.out.println("Exceptions:");
            for (CommandException exception : parse.getContext().getLastChild().getExceptions()) {
                for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                    System.out.println(stackTraceElement);
                }
            }
        }
        CommandContextBuilder<S> context = parse.getContext();
        if (context.getExceptions().size() != 0) {
            return new ArrayList<>();
        }
        while (context.getChild() != null && context.getChild().getExceptions().size() == 0) {
            context = context.getChild();
        }
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Last Valid Child:");
            context.print(0);
        }
        String partial = parse.getReader().getRemaining();
        List<String> suggestions = new ArrayList<>();

        /*
           If you have the following Structure:

           literal("test")
               some argument("create")
               greedy string

           And you type the following:

           "test c"

           It should autocomplete some argument (create)
         */

        if (context.getNode() instanceof ArgumentCommandNode<S, ?> argumentCommandNode
            && argumentCommandNode.getType() instanceof StringArgumentType<?> stringArgumentType
            && stringArgumentType.getType() == StringType.GREEDY_PHRASE) {
            //If current argument is StringArgumentType and Type GREEDY PHRASE get Parent Context
            CommandContextBuilder<S> parentContext = parse.getContext();
            while (!parentContext.getChild().equals(context)) {
                parentContext = parentContext.getChild();
            }
            for (CommandNode<S> child : parentContext.getNode().getChildren()) {
                if (!child.equals(context.getNode())) {
                    try {
                        String greedyPartial = parse.getReader().getString().substring(context.getRange().getStart(), context.getRange().getEnd());
                        suggestions.addAll(child.listSuggestions(context.build(greedyPartial), greedyPartial));
                    } catch (CommandException ignored) {
                    }
                }
            }
        }

        /*
           To prevent autocompletion in the following cases (example):
           literal("test")
              literal("abc")
           "test| " -> abc
           "test| a " -> abc
         */
        if ((!partial.equals(" ") && partial.endsWith(" ")) || partial.equals("")) {
            if (CommandAPI.DEBUG_MODE) {
                System.out.println("Return Empty Array");
            }
            return suggestions;
        }

        partial = partial.strip();
        Collection<CommandNode<S>> children = context.getNode().getChildren();
        int i = 0;
        for (CommandNode<S> child : children) {
            try {
                suggestions.addAll(child.listSuggestions(context.build(parse.getReader().getString().substring(0, parse.getReader().getCursor())), partial));
            } catch (CommandException ignored) {
            }
        }

        return suggestions;
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

            String commandString = "/" + branch.stream().filter(commandNode -> commandNode instanceof LiteralCommandNode<S>).map(CommandNode::getName).collect(Collectors.joining(" ")) + " ";
            TextComponent branchComponent = Texts.primary("/").append(Texts.primary(branch.get(0).getName()));
            for (int i = 1; i < branch.size(); i++) {
                boolean optional = branch.get(i - 1).getCommand() != null;
                TextComponent partialComponent = branch.get(i).getUsageText();
                if (optional) {
                    partialComponent = Texts.inferior("[").append(partialComponent).append(Texts.inferior("]"));
                }
                branchComponent = branchComponent.append(Component.space()).append(partialComponent);
            }
            if (lastNode.getDescription() != null) {
                branchComponent = branchComponent.append(Component.newline()).append(lastNode.getDescription());
            }
            branchComponent = branchComponent.clickEvent(ClickEvent.suggestCommand(commandString));
            textComponent = textComponent.append(Component.newline()).append(branchComponent);
        }
        return textComponent;
    }

}
