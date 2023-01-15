package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.tree.LiteralTreeCommand;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;

/**
 * Author: Placeblock
 */
public class ParseTestCommand extends Command<String> {
    public ParseTestCommand() {
        super((LiteralTreeCommand<String>) new LiteralTreeCommandBuilder<String>("party")
            .literal("add", addCommand -> addCommand
                .run(ctx -> {

                }))
            .literal("remove", addCommand -> addCommand
                .parameter("amount", new IntegerParameter<>(), amountParameter -> amountParameter
                    .run(ctx -> {

                    })))
            .build()
        );
    }
}
