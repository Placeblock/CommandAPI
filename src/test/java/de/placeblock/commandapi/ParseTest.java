package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParseContext;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
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
        ParseContext<String> context = parseTestCommand.parse("testcommandparse remove 22 ", "TestPlayer", false);
        System.out.println(context.getReader().debugString());
        System.out.println(context.getParsedCommands());
        System.out.println(context.getLastParsedCommand().getName());
        assert context.getReader().getCursor() == 26;
        assert context.getParsedCommands().size() != 0;
        assert context.getLastParsedCommand() instanceof ParameterTreeCommand<?,?>;
        context = parseTestCommand.parse("testcommandparse", "TestPlayer", false);
        assert context.getReader().getCursor() == 16;
        assert context.getParsedCommands().size() != 0;
        assert context.getLastParsedCommand() instanceof LiteralTreeCommand<String>;
    }

    @Test
    public void testSuggestions() {
        Command.LOGGER.setLevel(Level.FINE);
        ParseTestCommand parseTestCommand = new ParseTestCommand();
        ParseContext<String> context = parseTestCommand.parse("testcommandparse remove", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse remove  ", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse add awd", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandparse remove awd", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).isEmpty();
        System.out.println("ASSERTION ERRORRRRRRRRRR RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        context = parseTestCommand.parse("testcommandparse remove ", "TestPlayer", true);
        System.out.println(context.getLastParsedCommand().getName());
        System.out.println(context.isNoPermission());
        assert parseTestCommand.getSuggestions(context).contains("0");
        context = parseTestCommand.parse("testcommandparse rem", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).contains("remove");
        context = parseTestCommand.parse("testcommandparse ", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).containsAll(List.of("remove", "add"));
        context = parseTestCommand.parse("testcommandparse", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).isEmpty();
        context = parseTestCommand.parse("testcommandp", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).contains("testcommandparse");
        context = parseTestCommand.parse("", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).contains("testcommandparse");
        context = parseTestCommand.parse("testcommandparse remove 10", "TestPlayer", true);
        assert parseTestCommand.getSuggestions(context).contains("100");
        assert context.getParameter("amount", Integer.class).getValue() == 10;
    }

}
