package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class Command<S> {

    private final LiteralTreeCommand<S> base;

    public ParseContext<S> parse(String text, S source) {
        ParseContext<S> parseContext = new ParseContext<>(text, source);
        this.base.parseRecursive(parseContext);
        return parseContext;
    }

    public List<String> getSuggestions(ParseContext<S> parseContext) {
        String text = parseContext.getText();
        if (parseContext.getCursor() > text.length()) return new ArrayList<>();
        String wrongInformation = text.substring(parseContext.getCursor());
        if (!wrongInformation.contains(" ")) {
            TreeCommand<S> lastParsedCommand = parseContext.getLastParsedCommand();
            if (lastParsedCommand == null) {
                return this.base.getSuggestions(parseContext);
            }
            List<String> suggestions = new ArrayList<>();
            for (TreeCommand<S> child : lastParsedCommand.getChildren()) {
                suggestions.addAll(child.getSuggestions(parseContext));
            }
            return suggestions;
        }
        return new ArrayList<>();
    }

}
