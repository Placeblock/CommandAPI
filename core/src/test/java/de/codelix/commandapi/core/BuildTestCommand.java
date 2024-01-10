package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.tree.Parameter;
import de.codelix.commandapi.core.tree.builder.LiteralCommandNodeBuilder;
import de.codelix.commandapi.core.tree.builder.ParameterCommandNodeBuilder;
import net.kyori.adventure.text.TextComponent;

import static de.codelix.commandapi.core.parameter.IntegerParameter.integer;

/**
 * Author: Placeblock
 */
public class BuildTestCommand extends Command<String> {

    public BuildTestCommand() {
        super("testcommandbuild", false);
    }

    @Override
    public LiteralCommandNodeBuilder<String> generateCommand(LiteralCommandNodeBuilder<String> builder) {
        return builder
        .then(
            parameter("amount", integer(0, 100))
        ).then(
            literal("test").then(
                literal("test2")
            )
        );
    }

    private static LiteralCommandNodeBuilder<String> literal(String name) {
        return new LiteralCommandNodeBuilder<>(name);
    }

    @SuppressWarnings("SameParameterValue")
    private static ParameterCommandNodeBuilder<String, ?> parameter(String name, Parameter<String, ?> parameter) {
        return new ParameterCommandNodeBuilder<>(name, parameter);
    }

    @Override
    public boolean hasPermission(String source, String permission) {
        return true;
    }

    @Override
    public void sendMessageRaw(String source, TextComponent message) {

    }
}
