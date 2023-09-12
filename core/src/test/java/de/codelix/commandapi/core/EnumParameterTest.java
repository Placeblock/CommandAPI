package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Author: Placeblock
 */
public class EnumParameterTest {
    @Test
    public void testEnumParameterSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "";
        List<ParsedCommandBranch<String>> results = parseTestCommand.parse("testcommandparse add ", source);
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
