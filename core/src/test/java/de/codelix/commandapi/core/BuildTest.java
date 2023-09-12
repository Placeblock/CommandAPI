package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parameter.IntegerParameter;
import de.codelix.commandapi.core.tree.ParameterTreeCommand;
import de.codelix.commandapi.core.tree.TreeCommand;
import org.junit.jupiter.api.Test;

/**
 * Author: Placeblock
 */
public class BuildTest {

    @Test()
    public void buildTest() {
        TreeCommand<String> parseTestCommand = new BuildTestCommand().getBase();
        assert parseTestCommand.getChildren().size() == 2;
        assert parseTestCommand.getName().equals("testcommandbuild");
        assert parseTestCommand.getDescription() == null;
        assert parseTestCommand.getPermission() == null;
        assert parseTestCommand.getCommandExecutor() == null;
        TreeCommand<String> child = parseTestCommand.getChildren().get(0);
        assert child.getName().equals("amount");
        assert ((ParameterTreeCommand<String, Integer>) child).getParameter() instanceof IntegerParameter<String>;
    }

}