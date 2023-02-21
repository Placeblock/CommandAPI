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

    public List<ParsedCommand<S>> parseRecursive(ParsedCommand<S> parsedCommand, S source) {
        List<ParsedCommand<S>> commands = new ArrayList<>();
        commands.add(parsedCommand);
        if (this.hasNoPermission(source)) {
            parsedCommand.addException(this, new CommandSyntaxException(Errors.INVALID_COMMAND));
            return commands;
        }
        try {
            this.parse(parsedCommand, source);
        } catch (CommandSyntaxException e) {
            parsedCommand.addException(this, e);
            return commands;
        }
        StringReader reader = parsedCommand.getReader();
        if (reader.canRead(2) && reader.peek(1) != ' ') {
            for (TreeCommand<S> child : this.getChildren()) {
                ParsedCommand<S> childParsedCommand = new ParsedCommand<>(parsedCommand);
                childParsedCommand.getReader().skip();
                child.parseRecursive(childParsedCommand, source);
                commands.add(childParsedCommand);
            }
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

    public int getShortestBranchLength(S source) {
        List<List<TreeCommand<S>>> branches = this.getBranches(source);
        int branchLength = Integer.MAX_VALUE;
        for (List<TreeCommand<S>> branch : branches) {
            branchLength = Math.min(branchLength, branch.size());
        }
        return branchLength;
    }

    public boolean hasNoPermission(S source) {
        if (this.permission == null) return false;
        return !this.command.hasPermission(source, this.permission);
    }

    public abstract List<String> getSuggestions(ParsedCommand<S> command, S source);

    public abstract TextComponent getHelpComponent();

    public abstract TextComponent getHelpExtraDescription();
}
