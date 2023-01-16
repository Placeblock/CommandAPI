package de.placeblock.commandapi;

import de.placeblock.commandapi.core.exception.CommandException;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.parser.ParseContext;
import org.junit.jupiter.api.Test;

/**
 * Author: Placeblock
 */
public class IntegerParameterTest {

    @Test()
    public void integerParameterTest() throws CommandException {
        IntegerParameter<String> integerParameter = new IntegerParameter<>();
        ParseContext<String> parseContext = new ParseContext<>("awdawd 123   ", "TestSource");
        parseContext.getReader().setCursor(7);
        Integer result = integerParameter.parse(parseContext, null);
        System.out.println(result);
        assert result != 0;
    }

}
