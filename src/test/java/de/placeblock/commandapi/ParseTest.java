package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParsedCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Level;

/**
 * Author: Placeblock
 */
public class ParseTest {

    @Test
    public void testParse() {
        Command.LOGGER.setLevel(Level.FINE);
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "TestPlayer";
        List<ParsedCommand<String>> results = parseTestCommand.parse("testcommandparse remove 22 ", source);
        ParsedCommand<String> result = Command.getBestResult(results);
        assert result.getReader().getCursor() == 26;
        assert result.getParsedTreeCommands().size() != 0;
        assert result.getLastParsedTreeCommand() instanceof ParameterTreeCommand<?,?>;
        results = parseTestCommand.parse("testcommandparse", source);
        result = Command.getBestResult(results);
        assert result.getReader().getCursor() == 16;
        assert result.getParsedTreeCommands().size() == 1;
        assert result.getLastParsedTreeCommand() != null;
    }

    @Test
    public void testSuggestions() {
        Command.LOGGER.setLevel(Level.FINE);
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        String source = "TestPlayer";
        List<ParsedCommand<String>> results = parseTestCommand.parse("testcommandparse remove", source);
        List<String> suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.isEmpty();
        results = parseTestCommand.parse("testcommandparse remove  ", source);
        assert parseTestCommand.getSuggestions(results, source).isEmpty();
        results = parseTestCommand.parse("testcommandparse add awd", source);
        assert parseTestCommand.getSuggestions(results, source).isEmpty();
        results = parseTestCommand.parse("testcommandparse remove awd", source);
        assert parseTestCommand.getSuggestions(results, source).isEmpty();
        results = parseTestCommand.parse("testcommandparse remove ", source);
        assert parseTestCommand.getSuggestions(results, source).contains("0");
        results = parseTestCommand.parse("testcommandparse rem", source);
        assert parseTestCommand.getSuggestions(results, source).contains("remove");
        results = parseTestCommand.parse("testcommandparse ", source);
        assert parseTestCommand.getSuggestions(results, source).containsAll(List.of("remove", "add"));
        results = parseTestCommand.parse("testcommandparse", source);
        assert parseTestCommand.getSuggestions(results, source).isEmpty();
        results = parseTestCommand.parse("testcommandparse remove 10", source);
        suggestions = parseTestCommand.getSuggestions(results, source);
        assert suggestions.contains("100") && !suggestions.contains("106");
    }

}
