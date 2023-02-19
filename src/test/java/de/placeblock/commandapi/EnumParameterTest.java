package de.placeblock.commandapi;

import de.placeblock.commandapi.core.parser.ParseContext;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Author: Placeblock
 */
public class EnumParameterTest {
    @Test
    public void testEnumParameterSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> parseContext = parseTestCommand.parse("testcommandparse add ", "", true);
        List<String> result = parseTestCommand.getSuggestions(parseContext);
        assert result.contains("TEST");

        parseContext = parseTestCommand.parse("testcommandparse add T", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.contains("TEST");

        parseContext = parseTestCommand.parse("testcommandparse add TEST", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.contains("TEST2");
        parseContext = parseTestCommand.parse("testcommandparse add ABCD", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.isEmpty();
    }
}
