package de.placeblock.commandapi.core;

import de.placeblock.commandapi.core.parameter.TestParameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.TreeCommandBuilder;

/**
 * Author: Placeblock
 */
public class TestCommand extends Command<String> {
    public TestCommand(LiteralTreeCommandBuilder<String> base) {
        super(base);

        new LiteralTreeCommandBuilder<String>("test")
            .literal("test2", testCommand -> testCommand
                .parameter("test3", new TestParameter(), test3Command -> test3Command
                    .run(ctx -> {

                    }))
                .literal("test34", test4Command -> {

                }))
            .literal("test2", subCommand -> {

            });
    }
}
