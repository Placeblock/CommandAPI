package de.codelix.commandapi.core.parseorder;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parameter.StringOfListParameter;
import de.codelix.commandapi.core.parameter.StringParameter;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.core.tree.builder.ParameterCommandNodeBuilder;
import net.kyori.adventure.text.TextComponent;

import java.util.List;

public class ParseOrderTestCommand extends Command<String> {
    public ParseOrderTestCommand() {
        super("parseorder");
    }

    @Override
    public LiteralCommandNodeBuilder<String> generateCommand(LiteralCommandNodeBuilder<String> builder) {
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

    private static ParameterCommandNodeBuilder<String, ?> parameter(String name, Parameter<String, ?> parameter) {
        return new ParameterCommandNodeBuilder<>(name, parameter);
    }
}
