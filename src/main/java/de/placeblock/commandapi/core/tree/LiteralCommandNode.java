package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.CommandAPI;
import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.exception.InvalidCommandException;
import de.placeblock.commandapi.core.util.StringRange;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter
public class LiteralCommandNode<S> extends CommandNode<S> {
    private final List<String> aliases;

    public LiteralCommandNode(String label, TextComponent description, List<String> aliases, List<String> permissions, Command<S> command, Predicate<S> requirement) {
        super(label, description, permissions, command, requirement);
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
        int start = reader.getCursor();
        if (reader.canRead(this.getName().length())) {
            if (CommandAPI.DEBUG_MODE) {
                System.out.println("Can Read");
            }
            int end = start + this.getName().length();
            String parsedLiteral = reader.getString().substring(start, end);
            if (CommandAPI.DEBUG_MODE) {
                System.out.println("parsed literal: '" + parsedLiteral + "'");
                System.out.println(this.getName());
                System.out.println(this.aliases);
            }
            if (parsedLiteral.equalsIgnoreCase(this.getName()) && (this.aliases == null || this.aliases.isEmpty() || new ArrayList<>(this.aliases).removeIf(alias -> alias.equalsIgnoreCase(parsedLiteral)))) {
                reader.setCursor(end);
                if (CommandAPI.DEBUG_MODE) {
                    System.out.println("IsEqual");
                }
                if (!reader.canRead() || reader.peek() == ' ') {
                    return end;
                } else {
                    if (CommandAPI.DEBUG_MODE) {
                        System.out.println("Reset Cursor");
                    }
                    reader.setCursor(start);
                }
            }
        }
        return -1;
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
