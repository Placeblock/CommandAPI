package de.placeblock.commandapi;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * Author: Placeblock
 */
public class IntegerParameterTest {

    @Test()
    public void integerParameterTest() {
        IntegerParameter<String> integerParameter = new IntegerParameter<>(0, 100);
        ParseContext<String> parseContext = new ParseContext<>("awdawd 100   ", "TestSource");
        parseContext.getReader().setCursor(7);
        ParsedValue<Integer> result = integerParameter.parse(parseContext, null);
        assert !result.hasException() && Objects.equals(result.getParsed(), 100);
    }

    @Test()
    public void integerErrorParameterTest() {
        IntegerParameter<String> integerParameter = new IntegerParameter<>(0, 100);
        ParseContext<String> parseContext = new ParseContext<>("awdawd 123   ", "TestSource");
        parseContext.getReader().setCursor(7);
        ParsedValue<Integer> result = integerParameter.parse(parseContext, null);
        assert result.hasException();
    }

}
