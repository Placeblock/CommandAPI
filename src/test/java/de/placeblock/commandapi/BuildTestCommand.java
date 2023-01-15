package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;

/**
 * Author: Placeblock
 */
public class BuildTestCommand extends Command<String> {
    public BuildTestCommand() {
        super((LiteralTreeCommand<String>) new LiteralTreeCommandBuilder<String>("party")
                .parameter("amount", new IntegerParameter<>(), amountParameter -> {})
            .build()
        );
    }
}
