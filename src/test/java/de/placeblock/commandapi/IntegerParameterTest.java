package de.placeblock.commandapi;

import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.parser.StringReader;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * Author: Placeblock
 */
public class IntegerParameterTest {

    @Test()
    public void integerParameterTest() throws CommandSyntaxException {
        IntegerParameter<String> integerParameter = new IntegerParameter<>(0, 100);
        StringReader reader = new StringReader("awdawd 100   ");
        reader.setCursor(7);
        ParsedCommand<String> parsedCommand = new ParsedCommand<>(reader);
        Integer result = integerParameter.parse(parsedCommand);
        assert Objects.equals(result, 100);
    }

    @Test()
    public void integerErrorParameterTest() {
        IntegerParameter<String> integerParameter = new IntegerParameter<>(0, 100);
        StringReader reader = new StringReader("awdawd 123   ");
        reader.setCursor(7);
        ParsedCommand<String> parsedCommand = new ParsedCommand<>(reader);
        try {
            integerParameter.parse(parsedCommand);
            assert false;
        } catch (CommandSyntaxException ignored) {}
    }

}
