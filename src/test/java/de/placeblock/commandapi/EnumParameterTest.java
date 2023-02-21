package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;

/**
 * Author: Placeblock
 */
public class EnumParameterTest {
    @Test
    public void testEnumParameterSuggestions() {
        Command.LOGGER.setLevel(Level.FINE);
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "";
        List<ParsedCommand<String>> results = parseTestCommand.parse("testcommandparse add ", source);
        List<String> suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("TEST");

        results = parseTestCommand.parse("testcommandparse add T", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("TEST");

        results = parseTestCommand.parse("testcommandparse add TEST", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("TEST2");
        results = parseTestCommand.parse("testcommandparse add ABCD", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();
    }
}
