package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parameter.IntegerParameter;
import de.codelix.commandapi.core.tree.CommandNode;
import de.codelix.commandapi.core.tree.ParameterCommandNode;
import org.junit.jupiter.api.Test;

/**
 * Author: Placeblock
 */
public class BuildTest {

    @Test()
    public void buildTest() {
        CommandNode<String> parseTestCommand = new BuildTestCommand().getBase();
        assert parseTestCommand.getChildren().size() == 2;
        assert parseTestCommand.getName().equals("testcommandbuild");
        assert parseTestCommand.getDescription() == null;
        assert parseTestCommand.getPermission() == null;
        assert parseTestCommand.getCommandExecutor() == null;
        CommandNode<String> child = parseTestCommand.getChildren().get(0);
        assert child.getName().equals("amount");
        assert ((ParameterCommandNode<String, Integer>) child).getParameter() instanceof IntegerParameter<String>;
    }

}
