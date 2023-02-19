package de.placeblock.commandapi;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.parser.ParsedValue;
import org.junit.jupiter.api.Test;

/**
 * Author: Placeblock
 */
public class StringParameterTest {

    @Test
    public void testGreedyStringParameterParse() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> parseContext = parseTestCommand.parse("testcommandparse greedy a", "", false);
        ParsedValue<String> parsedGreedy = parseContext.getParameter("greedy", String.class);
        assert "a".equals(parsedGreedy.getValue());
        assert "a".equals(parsedGreedy.getString());

        parseContext = parseTestCommand.parse("testcommandparse greedy a awd awdaw awd", "", false);
        parsedGreedy = parseContext.getParameter("greedy", String.class);
        assert "a awd awdaw awd".equals(parsedGreedy.getValue());
        assert "a awd awdaw awd".equals(parsedGreedy.getString());
    }
}
