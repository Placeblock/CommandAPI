package de.placeblock.commandapi;

import de.placeblock.commandapi.core.exception.CommandParseException;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.parser.ParsedCommandBranch;
import de.placeblock.commandapi.core.parser.StringReader;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * Author: Placeblock
 */
public class IntegerParameterTest {

    @Test()
    public void integerParameterTest() throws CommandParseException {
        IntegerParameter<String> integerParameter = new IntegerParameter<>(0, 100);
        StringReader reader = new StringReader("awdawd 100   ");
        reader.setCursor(7);
        ParsedCommandBranch<String> parsedCommandBranch = new ParsedCommandBranch<>(reader);
        Integer result = integerParameter.parse(parsedCommandBranch, "source");
        assert Objects.equals(result, 100);
    }

    @Test()
    public void integerErrorParameterTest() {
        IntegerParameter<String> integerParameter = new IntegerParameter<>(0, 100);
        StringReader reader = new StringReader("awdawd 123   ");
        reader.setCursor(7);
        ParsedCommandBranch<String> parsedCommandBranch = new ParsedCommandBranch<>(reader);
        try {
            integerParameter.parse(parsedCommandBranch, "source");
            assert false;
        } catch (CommandParseException ignored) {}
    }

}
