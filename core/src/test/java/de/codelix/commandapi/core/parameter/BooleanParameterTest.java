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
public class BooleanParameterTest {
    @Test
    public void testBooleanParameterParse() throws CommandParseException {
        String source = "test";
        BooleanParameter<String> booleanParameter = new BooleanParameter<>();
        StringReader reader = new StringReader("awdawd f");
        reader.setCursor(7);
        ParsedCommandBranch<String> parsedCommandBranch = new ParsedCommandBranch<>(reader);
        try {
            booleanParameter.parse(parsedCommandBranch, source);
            assert false;
        } catch (CommandParseException ignored) {}

        reader = new StringReader("awdawd false");
        reader.setCursor(7);
        parsedCommandBranch = new ParsedCommandBranch<>(reader);
        Boolean result = booleanParameter.parse(parsedCommandBranch, source);
        assert Objects.equals(result, false);

        reader = new StringReader("awdawd true");
        reader.setCursor(7);
        parsedCommandBranch = new ParsedCommandBranch<>(reader);
        result = booleanParameter.parse(parsedCommandBranch, source);
        assert Objects.equals(result, true);

        reader = new StringReader("awdawd truea");
        reader.setCursor(7);
        parsedCommandBranch = new ParsedCommandBranch<>(reader);
        try {
            booleanParameter.parse(parsedCommandBranch, source);
            assert false;
        } catch (CommandParseException ignored) {}
    }

    @Test
    public void testBooleanParameterSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "";
        List<ParsedCommandBranch<String>> results = parseTestCommand.parse("testcommandparse bool t", source);
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
