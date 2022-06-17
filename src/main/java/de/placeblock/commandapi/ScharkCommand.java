package de.placeblock.commandapi;

import io.schark.design.Texts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
@SuppressWarnings("unused")
public abstract class ScharkCommand<P, C> {
    private ScharkCommand<P, C> parentCommand;
    private final String label;
    private final String description;
    private final String permission;
    private final List<ScharkCommand<P, C>> subCommands = new ArrayList<>();
    private final List<String> arguments = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final boolean showWithoutSubcommands;



    public ScharkCommand<P, C> addSubCommand(ScharkCommand<P, C> command) {
        command.parentCommand = this;
        this.subCommands.add(command);
        return this;
    }

    public ScharkCommand<P, C> addArgument(String argument) {
        this.arguments.add(argument);
        return this;
    }

    public ScharkCommand<P, C> addArguments(String... arguments) {
        this.arguments.addAll(List.of(arguments));
        return this;
    }

    public ScharkCommand<P, C> addAlias(String aliases) {
        this.aliases.add(aliases);
        return this;
    }

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
        TextComponent textComponent = Texts.generateWrappedText(this.label.substring(0, 1).toUpperCase() + this.label.substring(1), 40);
        return textComponent.append(Component.newline()).append(this.generateHelpRaw());
    }

    private String generateArgumentsString() {
        return this.getArguments().stream().collect(Collectors.joining("] [", "[", "]"));
    }

    private TextComponent generateHelpRaw() {
        List<ScharkCommand<P, C>> recursiveCommands = this.getCommandsRecursive();
        TextComponent commandComponent = Texts.primary("/").append(Texts.primary(recursiveCommands.get(0).getLabel()));
        commandComponent = commandComponent.append(Texts.secondary(recursiveCommands.get(0).generateArgumentsString()));
        for (int i = 1; i < recursiveCommands.size(); i++) {
            commandComponent = commandComponent.append(Texts.secondary(recursiveCommands.get(i).getLabel()));
            commandComponent = commandComponent.append(Texts.secondary(recursiveCommands.get(i).generateArgumentsString()));
        }
        Component argumentsComponent = Texts.secondary(this.generateArgumentsString());
        if (this.arguments.size() > 0) {
            commandComponent = commandComponent.append(argumentsComponent);
        }
        TextComponent textComponent = Component.empty();
        if (this.subCommands.size() == 0 || this.showWithoutSubcommands) {
            textComponent = textComponent.append(commandComponent).append(Component.newline()).append(Texts.secondary("   > " + this.description)).append(Component.newline());
            textComponent = textComponent.clickEvent(ClickEvent.suggestCommand(this.getCommandsRecursive().stream().map(ScharkCommand::getLabel).collect(Collectors.joining()) + " "));
        }
        for (ScharkCommand<P, C> subCommand : this.subCommands) {
            TextComponent subCommandComponent = subCommand.generateHelpRaw();
            textComponent = textComponent.append(subCommandComponent);
        }
        return textComponent;
    }

    public void execute(P player, C console, List<String> currentargs, List<String> allargs) {
        if (this.permission != null && !this.hasPermission(player, this.permission)) {
            this.sendNoPermMessage(player);
            return;
        }
        if (currentargs.size() < this.arguments.size()) {
            this.sendHelpMessage(player);
            return;
        }
        allargs.addAll(currentargs.subList(0, this.arguments.size()));
        if (currentargs.size() > this.arguments.size()) {
            for (ScharkCommand<P, C> subCommand : this.subCommands) {
                if (subCommand.getLabel().equals(currentargs.get(this.arguments.size())) || subCommand.getAliases().contains(currentargs.get(this.arguments.size()))) {
                    subCommand.execute(player, console, currentargs.subList(this.arguments.size(), currentargs.size()), allargs);
                }
                return;
            }
        }
        if (player == null) {
            this.onExecuteConsole(console, currentargs);
        } else {
            this.onExecutePlayer(player, currentargs);
        }
    }

    public void onExecuteConsole(C console, List<String> args) {}
    public abstract void onExecutePlayer(P player, List<String> args);

    public abstract boolean hasPermission(P player, String permission);
    public abstract void sendHelpMessage(P player);
    public abstract void sendNoPermMessage(P player);

    public Set<String> getExtraTabCompletions(P player, int argindex) {
        return new LinkedHashSet<>();
    }

    public Set<String> getTabCompletions(P player, String[] args) {
        Set<String> tabCompleteArgs = new LinkedHashSet<>(this.subCommands.stream().map(ScharkCommand::getLabel).toList());
        if (args.length > this.arguments.size()) {
            for (ScharkCommand<P, C> subCommand : this.subCommands) {
                if (subCommand.getLabel().equals(args[this.arguments.size()]) || subCommand.getAliases().contains(args[this.arguments.size()])) {
                    return subCommand.getTabCompletions(player, Arrays.copyOfRange(args, this.arguments.size(), args.length));
                }
                if (subCommand.getLabel().startsWith(args[this.arguments.size()])) {
                    tabCompleteArgs.add(subCommand.getLabel());
                }
                for (String alias : subCommand.getAliases()) {
                    if (alias.startsWith(args[this.arguments.size()])) {
                        tabCompleteArgs.add(alias);
                    }
                }
            }
        }
        for (String extraTabCompletion : this.getExtraTabCompletions(player, args.length - 1)) {
            if (extraTabCompletion.startsWith(args[this.arguments.size()])) {
                tabCompleteArgs.add(extraTabCompletion);
            }
        }
        if (tabCompleteArgs.size() == 0) {
            tabCompleteArgs.add("[" + this.arguments.get(args.length) + "]");
        }
        return tabCompleteArgs;
    }
}
