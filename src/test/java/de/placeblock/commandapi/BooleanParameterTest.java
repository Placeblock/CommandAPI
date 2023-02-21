package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.exception.CommandSyntaxException;
import de.placeblock.commandapi.core.parameter.BooleanParameter;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.parser.StringReader;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

/**
 * Author: Placeblock
 */
public class BooleanParameterTest {
    @Test
    public void testBooleanParameterParse() throws CommandSyntaxException {
        BooleanParameter<String> booleanParameter = new BooleanParameter<>();
        StringReader reader = new StringReader("awdawd f");
        reader.setCursor(7);
        ParsedCommand<String> parsedCommand = new ParsedCommand<>(reader);
        try {
            booleanParameter.parse(parsedCommand);
            assert false;
        } catch (CommandSyntaxException ignored) {}

        reader = new StringReader("awdawd false");
        reader.setCursor(7);
        parsedCommand = new ParsedCommand<>(reader);
        Boolean result = booleanParameter.parse(parsedCommand);
        assert Objects.equals(result, false);

        reader = new StringReader("awdawd true");
        reader.setCursor(7);
        parsedCommand = new ParsedCommand<>(reader);
        result = booleanParameter.parse(parsedCommand);
        assert Objects.equals(result, true);

        reader = new StringReader("awdawd truea");
        reader.setCursor(7);
        parsedCommand = new ParsedCommand<>(reader);
        try {
            booleanParameter.parse(parsedCommand);
            assert false;
        } catch (CommandSyntaxException ignored) {}
    }

    @Test
    public void testBooleanParameterSuggestions() {
        Command.LOGGER.setLevel(Level.FINE);
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "";
        List<ParsedCommand<String>> results = parseTestCommand.parse("testcommandparse bool t", source);
        List<String> suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("true") && !suggestions.contains("false");

        results = parseTestCommand.parse("testcommandparse bool f", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("false") && !suggestions.contains("true");

        results = parseTestCommand.parse("testcommandparse bool falsea", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();
    }

}
