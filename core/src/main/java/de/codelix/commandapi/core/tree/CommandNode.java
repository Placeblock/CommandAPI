package de.codelix.commandapi.core.tree;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.CommandExecutor;
import de.codelix.commandapi.core.exception.CommandNoPermissionException;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.parser.StringReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public abstract class CommandNode<S> {

    private final Command<S> command;
    private final String name;
    private final TextComponent displayName;
    private final List<CommandNode<S>> children;
    private final TextComponent description;
    private final String permission;
    private final CommandExecutor<S> commandExecutor;

    protected abstract void parse(ParsedCommandBranch<S> command, S source) throws CommandParseException;

    public List<ParsedCommandBranch<S>> parseRecursive(ParsedCommandBranch<S> command, S source) {
        int cursor = command.getReader().getCursor();
        List<ParsedCommandBranch<S>> commands = new ArrayList<>();
        commands.add(command);
        if (this.hasNoPermission(source)) {
            command.setException(this, new CommandNoPermissionException(this));
            return commands;
        }
        try {
            Command.LOGGER.info("Parsing Command " + this.getName());
            this.parse(command, source);
        } catch (CommandParseException e) {
            Command.LOGGER.info("Error Parsing Command " + this.getName());
            e.setCommandNode(this);
            command.setException(this, e);
            // If an error occured while parsing we reset the cursor and subtract -1 because of the whitespace
            command.getReader().setCursor(cursor - 1);
            return commands;
        }
        StringReader reader = command.getReader();
        // We only parse Children if we can read further
        if (reader.canReadWord()) {
            for (CommandNode<S> child : this.getChildren()) {
                Command.LOGGER.info("Parsing Child " + child.getName());
                ParsedCommandBranch<S> childParsedCommandBranch = new ParsedCommandBranch<>(command);
                childParsedCommandBranch.getReader().skip();
                List<ParsedCommandBranch<S>> childParsedCommandBranches = child.parseRecursive(childParsedCommandBranch, source);
                commands.addAll(childParsedCommandBranches);
            }
        }
        Command.LOGGER.info("Commands of TreeCommand " + this.name);
        for (ParsedCommandBranch<S> parsedCommandBranch : commands) {
            Command.LOGGER.info(parsedCommandBranch.getBranch().stream().map(CommandNode::getName).toList() + ": " + parsedCommandBranch.getReader().debugString());
        }
        return commands;
    }

    public List<List<CommandNode<S>>> getBranches(S source) {
        List<List<CommandNode<S>>> branches = new ArrayList<>();
        if (this.children.size() == 0 || this.getCommandExecutor() != null) {
            branches.add(new ArrayList<>(List.of(this)));
        }
        for (CommandNode<S> child : this.children) {
            if (child.hasNoPermission(source)) continue;
            List<List<CommandNode<S>>> childBranches = child.getBranches(source);
            for (List<CommandNode<S>> childBranch : childBranches) {
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

    public abstract List<String> getSuggestions(ParsedCommandBranch<S> command, S source);

    public TextComponent getDisplayName() {
        if (this.displayName == null) {
            return Component.text(this.name);
        }
        return this.displayName;
    }
}
