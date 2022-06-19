package de.placeblock.commandapi;

import io.schark.design.Colors;
import io.schark.design.Texts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@SuppressWarnings("unused")
public abstract class ScharkCommand<P, C> {
    public static final String DESCRIPTION_PREFIX = "   > ";
    private ScharkCommand<P, C> parentCommand;
    private final String label;
    private final String description;
    private final String permission;
    private final List<ScharkCommand<P, C>> subCommands = new ArrayList<>();
    private final List<CommandArgument<P>> arguments = new ArrayList<>();
    private final List<CommandArgument<P>> optionalarguments = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final boolean showWithoutSubcommands;


    public ScharkCommand<P, C> addSubCommand(ScharkCommand<P, C> command) {
        command.parentCommand = this;
        this.subCommands.add(command);
        if (this.arguments.size() > 0 && !showWithoutSubcommands) {
            System.out.println("WARNING: Added SubCommand " + command.getLabel() + " to ScharkCommand " + this.getCommandsRecursive()
                + ". This ScharkCommand has Arguments, which will be ignored from now");
        }
        return this;
    }

    public ScharkCommand<P, C> addArgument(CommandArgument<P> argument) {
        this.addArguments(argument);
        return this;
    }

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final ScharkCommand<P, C> addArguments(CommandArgument<P>... arguments) {
        this.arguments.addAll(List.of(arguments));
        if (this.subCommands.size() > 0 && !showWithoutSubcommands) {
            System.out.println("WARNING: Added Arguments " + Arrays.toString(arguments) + " to ScharkCommand " + this.getCommandsRecursive()
                + ". This won't have any effect as this ScharkCommand has SubCommands");
        }
        return this;
    }

    public ScharkCommand<P, C> addOptionalArgument(CommandArgument<P> argument) {
        this.addOptionalArguments(argument);
        return this;
    }

    @SafeVarargs
    @SuppressWarnings("UnusedReturnValue")
    public final ScharkCommand<P, C> addOptionalArguments(CommandArgument<P>... arguments) {
        this.optionalarguments.addAll(List.of(arguments));
        if (this.subCommands.size() > 0 && !showWithoutSubcommands) {
            System.out.println("WARNING: Added Optional Arguments " + Arrays.toString(arguments) + " to ScharkCommand " + this.getCommandsRecursive()
                + ". This won't have any effect as this ScharkCommand has SubCommands");
        }
        return this;
    }

    public ScharkCommand<P, C> addAlias(String alias) {
        this.addAliases(alias);
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public ScharkCommand<P, C> addAliases(String... aliases) {
        this.aliases.addAll(List.of(aliases));
        return this;
    }

    public ScharkCommand<P, C> getParentRecursive() {
        if (this.parentCommand == null) {
            return this;
        }
        return this.parentCommand.getParentRecursive();
    }

    public List<ScharkCommand<P, C>> getCommandsRecursive() {
        List<ScharkCommand<P, C>> commands = new ArrayList<>();
        if (this.parentCommand != null) {
            commands.addAll(this.parentCommand.getCommandsRecursive());
        }
        commands.add(this);
        return commands;
    }


    public TextComponent generateHelp() {
        TextComponent textComponent = Texts.headline(this.label.substring(0, 1).toUpperCase() + this.label.substring(1), Colors.SPECIAL, 40);
        return textComponent.append(Component.newline()).append(this.generateHelpRaw());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private TextComponent generateHelpRaw() {
        List<ScharkCommand<P, C>> recursiveCommands = this.getCommandsRecursive();
        TextComponent commandComponent = Texts.primary("/").append(Texts.primary(recursiveCommands.get(0).getLabel())).append(Component.space());
        for (int i = 1; i < recursiveCommands.size(); i++) {
            commandComponent.append(Texts.secondary(recursiveCommands.get(i).getLabel()));
        }
        for (CommandArgument<P> argument : this.arguments) {
            commandComponent = commandComponent.append(Component.space().append(Texts.secondary("<" + argument.getLabel() + ">")
                .hoverEvent(HoverEvent.showText(Texts.secondary(argument.getDescription())))));
            commandComponent.append(Component.space());
        }
        for (CommandArgument<P> optionalArgument : this.optionalarguments) {
            commandComponent = commandComponent.append(Component.space().append(Texts.secondary("[" + optionalArgument.getLabel() + "]")
                .hoverEvent(HoverEvent.showText(Texts.secondary(optionalArgument.getDescription())))));
            commandComponent.append(Component.space());
        }
        TextComponent textComponent = Component.empty();
        if (this.subCommands.size() == 0 || this.showWithoutSubcommands) {
            textComponent.append(commandComponent).append(Component.newline()).append(Texts.secondary(DESCRIPTION_PREFIX + this.description)).append(Component.newline());
            textComponent.clickEvent(ClickEvent.suggestCommand("/" + this.getCommandsRecursive().stream().map(ScharkCommand::getLabel).collect(Collectors.joining()) + " "));
        }
        for (ScharkCommand<P, C> subCommand : this.subCommands) {
            TextComponent subCommandComponent = subCommand.generateHelpRaw();
            textComponent.append(subCommandComponent);
        }
        return textComponent;
    }

    public void execute(P player, C console, List<String> currentargs) {
        if (this.permission != null && !this.hasPermission(player, this.permission)) {
            this.sendNoPermMessage(player);
            return;
        }
        if (currentargs.size() < this.arguments.size()) {
            this.sendHelpMessage(player);
            return;
        }
        if ((this.arguments.size() + this.optionalarguments.size() == 0 || this.showWithoutSubcommands) && currentargs.size() > 0) {
            for (ScharkCommand<P, C> subCommand : this.subCommands) {
                if (subCommand.getLabel().equals(currentargs.get(0)) || subCommand.getAliases().contains(currentargs.get(0))) {
                    subCommand.execute(player, console, currentargs.subList(1, currentargs.size()));
                    return;
                }
            }
        }
        if (currentargs.size() >= this.arguments.size() && currentargs.size() <= this.arguments.size() + this.optionalarguments.size()) {
            if (player == null) {
                this.onExecuteConsole(console, currentargs);
            } else {
                this.onExecutePlayer(player, currentargs);
            }
        } else {
            this.sendHelpMessage(player);
        }
    }

    public void onExecuteConsole(C console, List<String> args) {
    }

    public abstract void onExecutePlayer(P player, List<String> args);

    public abstract boolean hasPermission(P player, String permission);

    public abstract void sendHelpMessage(P player);

    public abstract void sendNoPermMessage(P player);

    public Set<String> getTabCompletions(P player, String[] args) {
        System.out.println(args.length);
        Set<String> tabCompleteArgs = new LinkedHashSet<>();
        if (args.length <= this.arguments.size() + this.optionalarguments.size()) {
            CommandArgument<P> argument = args.length - 1 < this.arguments.size() ? this.arguments.get(args.length - 1) : this.optionalarguments.get(args.length - 1);
            for (String extraTabCompletion : argument.getExtraTabCompletions(player)) {
                if (extraTabCompletion.startsWith(args[args.length - 1])) {
                    tabCompleteArgs.add(extraTabCompletion);
                }
            }
        }
        if (args.length == 1) {
            for (ScharkCommand<P, C> subCommand : this.subCommands) {
                if (subCommand.getLabel().startsWith(args[0])) {
                    tabCompleteArgs.add(subCommand.getLabel());
                }
                for (String alias : subCommand.getAliases()) {
                    if (alias.startsWith(args[0])) {
                        tabCompleteArgs.add(alias);
                    }
                }
            }
        } else if (args.length > 1) {
            for (ScharkCommand<P, C> subCommand : this.subCommands) {
                if (subCommand.getLabel().equals(args[0]) || subCommand.getAliases().contains(args[0])) {
                    return subCommand.getTabCompletions(player, Arrays.copyOfRange(args, 1, args.length));
                }
            }
        }

        return tabCompleteArgs;
    }
}
