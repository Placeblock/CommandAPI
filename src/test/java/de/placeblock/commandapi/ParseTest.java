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
        ParseContext<String> context = parseTestCommand.parse("testcommandparse remove 22 ", "TestPlayer");
        assert context.getCursor() == 27;
        assert context.getLastParsedCommand() instanceof ParameterTreeCommand<?,?>;
        context = parseTestCommand.parse("testcommandparse", "TestPlayer");
        assert context.getCursor() == 17;
        assert context.getLastParsedCommand() instanceof LiteralTreeCommand<String>;
    }

    @Test
    public void testSuggestions() {
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> context = parseTestCommand.parse("testcommandparse remove", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse remove  ", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse add awd", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse remove awd", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse remove ", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("0");
        context = parseTestCommand.parse("testcommandparse rem", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("remove");
        context = parseTestCommand.parse("testcommandparse ", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).containsAll(List.of("remove", "add"));
        context = parseTestCommand.parse("testcommandparse", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandp", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("testcommandparse");
        context = parseTestCommand.parse("", "TestPlayer");
        assert parseTestCommand.getSuggestions(context).contains("testcommandparse");
    }

}
