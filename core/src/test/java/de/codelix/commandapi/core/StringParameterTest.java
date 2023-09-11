package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Author: Placeblock
 */
public class StringParameterTest {

    @Test
    public void testGreedyStringParameterParse() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "";
        List<ParsedCommandBranch<String>> results = parseTestCommand.parse("testcommandparse greedy a", source);
        ParsedCommandBranch<String> result = Command.getBestResult(results);
        String parsedGreedy = result.getParsedParameter("greedy", String.class);
        assert "a".equals(parsedGreedy);

        results = parseTestCommand.parse("testcommandparse greedy a awd awdaw awd", source);
        result = Command.getBestResult(results);
        parsedGreedy = result.getParsedParameter("greedy", String.class);
        assert "a awd awdaw awd".equals(parsedGreedy);
    }
}
