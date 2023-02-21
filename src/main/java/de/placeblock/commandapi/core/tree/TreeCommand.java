package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.CommandExecutor;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.parser.StringReader;
import io.schark.design.texts.Errors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public abstract class TreeCommand<S> {

    private final Command<S> command;
    private final String name;
    private final List<TreeCommand<S>> children;
    private final TextComponent description;
    private final String permission;
    private final CommandExecutor<S> commandExecutor;

    protected abstract void parse(ParsedCommand<S> command, S source) throws CommandSyntaxException;

    public List<ParsedCommand<S>> parseRecursive(ParsedCommand<S> command, S source) {
        int cursor = command.getReader().getCursor();
        List<ParsedCommand<S>> commands = new ArrayList<>();
        commands.add(command);
        if (this.hasNoPermission(source)) {
            command.addException(this, new CommandSyntaxException(Errors.INVALID_COMMAND));
            return commands;
        }
        try {
            Command.LOGGER.info("Parsing Command " + this.getName());
            this.parse(command, source);
        } catch (CommandSyntaxException e) {
            Command.LOGGER.info("Error Parsing Command " + this.getName());
            command.addException(this, e);
            // If an error occured while parsing we reset the cursor and subtract -1 because of the whitespace
            command.getReader().setCursor(cursor - 1);
            return commands;
        }
        StringReader reader = command.getReader();
        // We only parse Children if we can read further
        if (reader.canRead(2) && reader.peek(1) != ' ') {
            for (TreeCommand<S> child : this.getChildren()) {
                Command.LOGGER.info("Parsing Child " + child.getName());
                ParsedCommand<S> childParsedCommand = new ParsedCommand<>(command);
                childParsedCommand.getReader().skip();
                List<ParsedCommand<S>> childParsedCommands = child.parseRecursive(childParsedCommand, source);
                commands.addAll(childParsedCommands);
            }
        }
        Command.LOGGER.info("Commands of TreeCommand " + this.name);
        for (ParsedCommand<S> parsedCommand : commands) {
            Command.LOGGER.info(parsedCommand.getParsedTreeCommands().stream().map(TreeCommand::getName).toList() + ": " + parsedCommand.getReader().debugString());
        }
        return commands;
    }

    public List<List<TreeCommand<S>>> getBranches(S source) {
        List<List<TreeCommand<S>>> branches = new ArrayList<>();
        if (this.children.size() == 0 || this.getCommandExecutor() != null) {
            branches.add(new ArrayList<>(List.of(this)));
        }
        for (TreeCommand<S> child : this.children) {
            if (child.hasNoPermission(source)) continue;
            List<List<TreeCommand<S>>> childBranches = child.getBranches(source);
            for (List<TreeCommand<S>> childBranch : childBranches) {
                childBranch.add(0, this);
                branches.add(childBranch);
            }
        }
        return branches;
    }

    public boolean hasNoPermission(S source) {
        if (this.permission == null) return false;
        return !this.command.hasPermission(source, this.permission);
    }

    public abstract List<String> getSuggestions(ParsedCommand<S> command, S source);

    public abstract TextComponent getHelpComponent();

    public abstract TextComponent getHelpExtraDescription();
}
