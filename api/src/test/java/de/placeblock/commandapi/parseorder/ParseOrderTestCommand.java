package de.placeblock.commandapi.parseorder;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.Parameter;
import de.placeblock.commandapi.core.parameter.StringOfListParameter;
import de.placeblock.commandapi.core.parameter.StringParameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.ParameterTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public class ParseOrderTestCommand extends Command<String> {
    public ParseOrderTestCommand() {
        super("parseorder");
    }

    @Override
    public LiteralTreeCommandBuilder<String> generateCommand(LiteralTreeCommandBuilder<String> builder) {
        return builder
            .then(parameter("player", StringOfListParameter.stringoflist(List.of("felix", "paula")))
                .then(parameter("message", StringParameter.greedyString())
                    .run((data, s) ->
                        System.out.println(data.getParsedParameter("message")))
                )
            ).then(parameter("message",StringParameter.greedyString())
                .run((data, s) ->
                    System.out.println(data.getParsedParameter("message")))
            );
    }

    @Override
    public boolean hasPermission(String source, String permission) {
        return true;
    }

    @Override
    public void sendMessage(String source, TextComponent message) {
        System.out.println(message.content());
    }

    private static ParameterTreeCommandBuilder<String, ?> parameter(String name, Parameter<String, ?> parameter) {
        return new ParameterTreeCommandBuilder<>(name, parameter);
    }
}
