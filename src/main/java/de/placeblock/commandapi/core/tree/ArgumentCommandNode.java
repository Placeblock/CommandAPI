package de.placeblock.commandapi.core.tree;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.arguments.ArgumentType;
import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.context.CommandContextBuilder;
import de.placeblock.commandapi.core.context.ParsedArgument;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;
import io.schark.design.texts.Texts;
import lombok.Getter;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
public class ArgumentCommandNode<S, T> extends CommandNode<S> {
    private final ArgumentType<S, T> type;
    private final Function<String, List<String>> customSuggestions;

    public ArgumentCommandNode(String name, TextComponent description, List<String> permissions, Command<S> command, ArgumentType<S, T> type, Predicate<S> requirement, Function<String, List<String>> customSuggestions) {
        super(name, description, permissions, command, requirement);
        this.type = type;
        this.customSuggestions = customSuggestions;
    }

    @Override
    public void parse(StringReader reader, CommandContextBuilder<S> contextBuilder) throws CommandException {
        int start = reader.getCursor();
        T result = this.type.parse(contextBuilder.getSource(), reader);
        ParsedArgument<S, T> parsed = new ParsedArgument<>(start, reader.getCursor(), result);
        contextBuilder.withArgument(this.getName(), parsed);
        contextBuilder.withNode(this, parsed.getRange());
    }

    @Override
    public List<String> listSuggestions(final CommandContext<S> context, String partial) {
        if (customSuggestions == null) {
            return type.listSuggestions(context, partial);
        } else {
            return customSuggestions.apply(partial);
        }
    }

    public TextComponent getUsageText() {
        return Texts.inferior("<").append(Texts.inferior(this.getName())).append(Texts.inferior(">"));
    }
}
