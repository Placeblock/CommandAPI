package de.placeblock.commandapi;

import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.ParameterTreeCommand;
import de.placeblock.commandapi.core.tree.TreeCommand;
import org.junit.jupiter.api.Test;

/**
 * Author: Placeblock
 */
public class BuildTest {

    @Test()
    public void buildTest() {
        TreeCommand<String> parseTestCommand = new BuildTestCommand().getBase();
        assert parseTestCommand.getChildren().size() == 1;
        assert parseTestCommand.getName().equals("party");
        assert parseTestCommand.getDescription() == null;
        assert parseTestCommand.getPermissions().size() == 0;
        assert parseTestCommand.getRun() == null;
        TreeCommand<String> child = parseTestCommand.getChildren().get(0);
        assert child.getName().equals("amount");
        assert ((ParameterTreeCommand<String, Integer>) child).getParameter() instanceof IntegerParameter<String>;
    }

}
