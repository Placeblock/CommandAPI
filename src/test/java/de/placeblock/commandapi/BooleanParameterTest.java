package de.placeblock.commandapi;

import de.placeblock.commandapi.core.parameter.BooleanParameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

/**
 * Author: Placeblock
 */
public class BooleanParameterTest {
    @Test
    public void testBooleanParameterParse() {
        BooleanParameter<String> integerParameter = new BooleanParameter<>();
        ParseContext<String> parseContext = new ParseContext<>("awdawd f", "TestSource");
        parseContext.getReader().setCursor(7);
        ParsedValue<Boolean> result = integerParameter.parse(parseContext, null);
        assert result.hasException();

        parseContext = new ParseContext<>("awdawd false", "TestSource");
        parseContext.getReader().setCursor(7);
        result = integerParameter.parse(parseContext, null);
        assert !result.hasException() && Objects.equals(result.getValue(), false);

        parseContext = new ParseContext<>("awdawd true", "TestSource");
        parseContext.getReader().setCursor(7);
        result = integerParameter.parse(parseContext, null);
        assert !result.hasException() && Objects.equals(result.getValue(), true);

        parseContext = new ParseContext<>("awdawd truea", "TestSource");
        parseContext.getReader().setCursor(7);
        result = integerParameter.parse(parseContext, null);
        assert result.hasException();
    }

    @Test
    public void testBooleanParameterSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> parseContext = parseTestCommand.parse("testcommandparse bool t", "");
        List<String> result = parseTestCommand.getSuggestions(parseContext);
        assert result.contains("true") && !result.contains("false");

        parseContext = parseTestCommand.parse("testcommandparse bool f", "");
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.contains("false") && !result.contains("true");

        parseContext = parseTestCommand.parse("testcommandparse bool falsea", "");
        result = parseTestCommand.getSuggestions(parseContext);
        assert result.isEmpty();
    }

}
