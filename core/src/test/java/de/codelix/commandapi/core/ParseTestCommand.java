package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.TestEnum;
import de.codelix.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.codelix.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

import static de.codelix.commandapi.core.parameter.BooleanParameter.bool;
import static de.codelix.commandapi.core.parameter.DoubleParameter.doubleParam;
import static de.codelix.commandapi.core.parameter.EnumParameter.enumparam;
import static de.codelix.commandapi.core.parameter.IntegerParameter.integer;
import static de.codelix.commandapi.core.parameter.StringParameter.greedyString;

/**
 * Author: Placeblock
 */
public class ParseTestCommand extends Command<String> {
    public ParseTestCommand() {
        super("testcommandparse", false);
    }

    @Override
    public LiteralTreeCommandBuilder<String> generateCommand(LiteralTreeCommandBuilder<String> builder) {
        return builder
        .then(
            literal("add").then(
                parameter("material", enumparam(TestEnum.class, TestEnum.values()))
            )
        ).then(
            literal("remove").then(
                parameter("amount", integer(0, 105))
                .run((context, source) ->
                    System.out.println(context.getParsedParameter("amount"))))
        ).then(
                literal("greedy").then(
                    parameter("greedy", greedyString())
                )
            )
        .then(
                literal("bool").then(
                    parameter("bool", bool())
                )
            )
        .then(
            parameter("double", doubleParam(0D, 105.5D))
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
        System.out.println(message.content());
    }
}
