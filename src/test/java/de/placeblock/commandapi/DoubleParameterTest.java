package de.placeblock.commandapi;

import de.placeblock.commandapi.core.parameter.DoubleParameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * Author: Placeblock
 */
public class DoubleParameterTest {

    @Test
    public void testDoubleParameterParse() {
        DoubleParameter<String> doubleParameter = new DoubleParameter<>(0D, 100D);
        ParseContext<String> parseContext = new ParseContext<>("awdawd 100   ", "TestSource");
        parseContext.getReader().setCursor(7);
        ParsedValue<Double> result = doubleParameter.parse(parseContext, null);
        assert !result.hasException() && Objects.equals(result.getValue(), 100D);
    }

    @Test
    public void testDoubleParameterSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> parseContext = parseTestCommand.parse("testcommandparse 100", "", true);
        List<String> result = parseTestCommand.getSuggestions(parseContext);
        assert result.contains(".") && !result.contains("1001") && !result.contains("1");

        parseContext = parseTestCommand.parse("testcommandparse 100.", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.containsAll(List.of("100.1", "100.2", "100.9")) && !result.contains(".");

        parseContext = parseTestCommand.parse("testcommandparse 100..", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.isEmpty();

        parseContext = parseTestCommand.parse("testcommandparse 100..3", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.isEmpty();

        parseContext = parseTestCommand.parse("testcommandparse 100.3", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.containsAll(List.of("100.31", "100.32", "100.39"));

        parseContext = parseTestCommand.parse("testcommandparse 105.", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.containsAll(List.of("105.0", "105.1", "105.2", "105.3", "105.4", "105.5")) && !result.contains("105.6");

        parseContext = parseTestCommand.parse("testcommandparse ..", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.isEmpty();

        parseContext = parseTestCommand.parse("testcommandparse ..2", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.isEmpty();

        parseContext = parseTestCommand.parse("testcommandparse .", "", true);
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.containsAll(List.of(".1", ".2", ".3", ".0", ".9")) && !result.contains(".");
    }
}
