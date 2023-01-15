package de.placeblock.commandapi;

import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Author: Placeblock
 */
public class ParseTest {

    @Test
    public void testParse() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> context = parseTestCommand.parse("party remove 22 ", "TestPlayer");
        assert context.getCursor() == 16;
        assert context.getLastParsedCommand() instanceof ParameterTreeCommand<?,?>;
        context = parseTestCommand.parse("party", "TestPlayer");
        assert context.getCursor() == 6;
        assert context.getLastParsedCommand() instanceof LiteralTreeCommand<String>;
    }

    @Test
    public void testSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> context = parseTestCommand.parse("party remove", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("party remove  ", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("party add awd", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("party remove awd", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("party remove ", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("0");
        context = parseTestCommand.parse("party rem", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("remove");
        context = parseTestCommand.parse("party ", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).containsAll(List.of("remove", "add"));
        context = parseTestCommand.parse("par", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("party");
        context = parseTestCommand.parse("", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("party");
    }

}
