package de.codelix.commandapi.core.parseorder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.core.tree.TreeCommand;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ParseOrderTest {

    @Test
    public void parseOrderTest() {
        ParseOrderTestCommand command = new ParseOrderTestCommand();

        List<ParsedCommandBranch<String>> parse = command.parse("parseorder felix message", "source");
        ParsedCommandBranch<String> bestResult = Command.getBestResult(parse);
        List<TreeCommand<String>> branch = bestResult.getBranch();
        assert branch.get(0).getName().equals("parseorder");
        assert branch.get(1).getName().equals("player");
        assert branch.get(2).getName().equals("message");
        assert bestResult.getParsedParameter("message", String.class).equals("message");

        parse = command.parse("parseorder awdaawd message", "source");
        bestResult = Command.getBestResult(parse);
        branch = bestResult.getBranch();
        assert branch.get(0).getName().equals("parseorder");
        assert branch.get(1).getName().equals("message");
        assert bestResult.getParsedParameter("message", String.class).equals("awdaawd message");
    }

}
