package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.CommandAPI;
import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.InvalidCommandException;
import de.placeblock.commandapi.core.util.StringRange;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter
public class LiteralCommandNode<S> extends CommandNode<S> {
    private final List<String> aliases;

    public LiteralCommandNode(String label, TextComponent description, List<String> aliases, List<String> permissions, Command<S> command, Predicate<S> requirement, boolean async, boolean recursiveAsync) {
        super(label, description, permissions, command, requirement, async, recursiveAsync);
        this.aliases = aliases;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandException {
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("try parsing literal '" + this.getName() + "'");
        }
        int start = reader.getCursor();
        int end = parse(reader);
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("End " + end);
        }
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }
        throw new InvalidCommandException();
    }

    private int parse(StringReader reader) {
        if (this.checkString(reader, this.getName())) {
            return reader.getCursor();
        }
        for (String alias : this.aliases) {
            if (this.checkString(reader, alias)) {
                return reader.getCursor();
            }
        }
        return -1;
    }

    private boolean checkString(StringReader reader, String string) {
        if (CommandAPI.DEBUG_MODE) {
            System.out.println("Checking String " + string);
        }
        if (reader.canRead(string.length())) {
            if (CommandAPI.DEBUG_MODE) {
                System.out.println("CanRead String " + string);
            }
            int start = reader.getCursor();
            int end = start + string.length();
            String parsedLiteral = reader.getString().substring(start, end);
            if (CommandAPI.DEBUG_MODE) {
                System.out.println("ParsedLiteral " + string);
            }
            if (parsedLiteral.equalsIgnoreCase(string)) {
                if (CommandAPI.DEBUG_MODE) {
                    System.out.println("IsEqual " + string);
                }
                reader.setCursor(end);
                if (!reader.canRead() || reader.peek() == ' ') {
                    return true;
                } else {
                    if (CommandAPI.DEBUG_MODE) {
                        System.out.println("Reset Cursor");
                    }
                    reader.setCursor(start);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void print(int index) {
        System.out.println(" ".repeat(index * 5) + "Name: " + this.getName());
        System.out.println(" ".repeat(index * 5) + "Command: " + this.getCommand());
        System.out.println(" ".repeat(index * 5) + "Aliases: " + this.aliases);
        for (CommandNode<S> childEntry : this.getChildren()) {
            childEntry.print(index + 1);
        }
    }

    @Override
    public List<String> listSuggestions(CommandContext<S> context, String partial) {
        if (this.getName().toLowerCase().startsWith(partial)) {
            return List.of(this.getName());
        } else {
            return new ArrayList<>();
        }
    }

    public TextComponent getUsageText() {
        return Texts.inferior(this.getName());
    }

}
