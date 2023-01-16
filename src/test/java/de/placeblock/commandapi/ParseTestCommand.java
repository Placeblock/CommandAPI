package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public class ParseTestCommand extends Command<String> {
    public ParseTestCommand() {
        super("testcommandparse");
    }

    @Override
    public LiteralTreeCommandBuilder<String> generateCommand(LiteralTreeCommandBuilder<String> builder) {
        return builder
        .then(
            literal("add")
                .run(ctx -> {

                })
        ).then(
            literal("remove").then(
                parameter("amount", new IntegerParameter<>())
                .run(ctx -> {

                }))
        );
    }

    private static LiteralTreeCommandBuilder<String> literal(String name) {
        return new LiteralTreeCommandBuilder<>(name);
    }

    private static ParameterTreeCommandBuilder<String, ?> parameter(String name, Parameter<String, ?> parameter) {
        return new ParameterTreeCommandBuilder<>(name, parameter);
    }

    @Override
    public boolean hasPermission(String source, String permission) {
        return true;
    }

    @Override
    public void sendMessage(String source, TextComponent message) {

    }
}
