package de.placeblock.commandapi.arguments;

import de.placeblock.commandapi.context.CommandContext;
import de.placeblock.commandapi.exception.CommandSyntaxException;
import de.placeblock.commandapi.util.StringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BooleanArgumentType implements ArgumentType<Boolean> {
    @Override
    public Boolean parse(StringReader reader) throws CommandSyntaxException {
        return reader.readBoolean();
    }

    public static boolean getBoolean(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Boolean.class);
    }

    @Override
    public <S> CompletableFuture<List<String>> listSuggestions(CommandContext<S> context, String partial) {
        List<String> completions = new ArrayList<>();
        if ("true".startsWith(partial)) {
            completions.add("true");
        }
        if ("false".startsWith(partial)) {
            completions.add("false");
        }
        return CompletableFuture.completedFuture(completions);
    }
}
