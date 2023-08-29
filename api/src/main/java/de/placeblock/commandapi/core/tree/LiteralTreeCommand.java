package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.CommandExecutor;
import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.exception.InvalidLiteralException;
import de.placeblock.commandapi.core.parser.ParsedCommandBranch;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
public class LiteralTreeCommand<S> extends TreeCommand<S> {
    private final List<String> aliases;

    public LiteralTreeCommand(Command<S> command, String name, List<TreeCommand<S>> children, TextComponent description,
                              String permission, CommandExecutor<S>  run, List<String> aliases) {
        super(command, name, children, description, permission, run);
        this.aliases = aliases;
    }

    @Override
    protected void parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        String nextWord = command.getReader().readUnquotedString();
        command.getBranch().add(this);
        if (nextWord == null || (!nextWord.equalsIgnoreCase(this.getName()) &&
            this.aliases.stream().map(alias -> alias.equalsIgnoreCase(nextWord)).toList().size() == 0)) {
            throw new InvalidLiteralException();
        }
    }

    @Override
    public List<String> getSuggestions(ParsedCommandBranch<S> context, S source) {
        String partial = context.getReader().getRemaining();
        if (((!this.getName().startsWith(partial) || this.getName().equals(partial))
            && this.aliases.stream().filter(alias -> alias.startsWith(partial) && !alias.equals(partial)).toList().size() == 0)
            || this.hasNoPermission(source)) {
            return new ArrayList<>();
        }
        List<String> suggestions = new ArrayList<>(this.aliases);
        suggestions.add(this.getName());
        return suggestions;
    }

    @Override
    public TextComponent getHelpComponent() {
        return this.getCommand().getDesign().getHelpLiteralTreeCommand(this);
    }

    @Override
    public TextComponent getHelpExtraDescription() {
        return this.getCommand().getDesign().getHelpLiteralTreeCommandDescription(this);
    }
}