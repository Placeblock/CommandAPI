package de.placeblock.commandapi.core.arguments;

import de.placeblock.commandapi.core.context.CommandContext;
import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.util.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public class BooleanArgumentType implements ArgumentType<Boolean> {
    @Override
    public Boolean parse(StringReader reader) throws CommandException {
        return reader.readBoolean();
    }

    public static boolean getBoolean(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Boolean.class);
    }

    @Override
    public <S> CompletableFuture<List<String>> listSuggestions(CommandContext<S> context, String partial) {
        List<String> completions = new ArrayList<>();
        if ("true".startsWith(partial.toLowerCase())) {
            completions.add("true");
        }
        if ("false".startsWith(partial.toLowerCase())) {
            completions.add("false");
        }
        return CompletableFuture.completedFuture(completions);
    }
}
