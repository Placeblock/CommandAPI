package de.placeblock.commandapi;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parameter.DoubleParameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Author: Placeblock
 */
public class DoubleParameterTest {
    public static final List<String> NUMBER_SUGGESTIONS = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");

    @Test
    public void testDoubleParameterParse() throws CommandException {
        DoubleParameter<String> doubleParameter = new DoubleParameter<>(0D, 100D);
        ParseContext<String> parseContext = new ParseContext<>("awdawd 100   ", "TestSource");
        parseContext.getReader().setCursor(7);
        Double result = doubleParameter.parse(parseContext, null);
        assert result != 0;
    }

    @Test
    public void testDoubleParameterSuggestions() throws CommandException {
        DoubleParameter<String> doubleParameter = new DoubleParameter<>(0D, 105.5D);

        ParseContext<String> parseContext = new ParseContext<>("100", "TestSource");
        List<String> result = doubleParameter.getSuggestions(parseContext, null);
        assert result.contains(".") && !result.contains("1001") && !result.contains("1");

        parseContext = new ParseContext<>("100.", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.containsAll(List.of("100.1", "100.2", "100.9")) && !result.contains(".");

        parseContext = new ParseContext<>("100..", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.isEmpty();

        parseContext = new ParseContext<>("100..3", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.isEmpty();

        parseContext = new ParseContext<>("100.3", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.containsAll(List.of("100.31", "100.32", "100.39"));

        parseContext = new ParseContext<>("105.", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.containsAll(List.of("105.0", "105.1", "105.2", "105.3", "105.4", "105.5")) && !result.contains("105.6");

        parseContext = new ParseContext<>("105.", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.containsAll(List.of("105.0", "105.1", "105.2", "105.3", "105.4", "105.5")) && !result.contains("105.6");

        parseContext = new ParseContext<>("..", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.isEmpty();

        parseContext = new ParseContext<>("..2", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.isEmpty();

        parseContext = new ParseContext<>("..2", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        assert result.isEmpty();

        parseContext = new ParseContext<>(".", "TestSource");
        result = doubleParameter.getSuggestions(parseContext, null);
        System.out.println(result);
        assert result.containsAll(List.of(".1", ".2", ".3", ".0", ".9")) && !result.contains(".");
    }
}
