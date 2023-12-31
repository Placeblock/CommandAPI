package de.codelix.commandapi.core.parameter;

import de.codelix.commandapi.core.ParseTestCommand;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.parser.StringReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * Author: Placeblock
 */
public class DoubleParameterTest {

    @Test
    public void testDoubleParameterParse() throws CommandParseException {
        DoubleParameter<String> doubleParameter = new DoubleParameter<>(0D, 100D);
        StringReader reader = new StringReader("awdawd 100   ");
        reader.setCursor(7);
        ParsedCommandBranch<String> parsedCommandBranch = new ParsedCommandBranch<>(reader);
        Double result = doubleParameter.parse(parsedCommandBranch, "source");
        assert Objects.equals(result, 100D);
    }

    @Test
    public void testDoubleParameterSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "";
        List<ParsedCommandBranch<String>> results = parseTestCommand.parse("testcommandparse 100", source);
        List<String> suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("100.") && !suggestions.contains("1001") && !suggestions.contains("1");

        results = parseTestCommand.parse("testcommandparse 100.", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.containsAll(List.of("100.1", "100.2", "100.9")) && !suggestions.contains(".");

        results = parseTestCommand.parse("testcommandparse 100..", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();

        results = parseTestCommand.parse("testcommandparse 100..3", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();

        results = parseTestCommand.parse("testcommandparse 100.3", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.containsAll(List.of("100.31", "100.32", "100.39"));

        results = parseTestCommand.parse("testcommandparse 105.", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.containsAll(List.of("105.0", "105.1", "105.2", "105.3", "105.4", "105.5")) && !suggestions.contains("105.6");

        results = parseTestCommand.parse("testcommandparse ..", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();

        results = parseTestCommand.parse("testcommandparse ..2", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();

        results = parseTestCommand.parse("testcommandparse .", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.containsAll(List.of(".1", ".2", ".3", ".0", ".9")) && !suggestions.contains(".");

        results = parseTestCommand.parse("testcommandparse ", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.containsAll(List.of(".", "1"));
    }
}
