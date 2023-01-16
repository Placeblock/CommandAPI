package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

import static de.placeblock.commandapi.core.parameter.IntegerParameter.integer;

/**
 * Author: Placeblock
 */
public class BuildTestCommand extends Command<String> {

    public BuildTestCommand() {
        super("testcommandbuild");
    }

    @Override
    public LiteralTreeCommandBuilder<String> generateCommand(LiteralTreeCommandBuilder<String> builder) {
        return builder
        .then(
            parameter("amount", integer(0, 100))
        ).then(
            literal("test").then(
                literal("test2")
            )
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
