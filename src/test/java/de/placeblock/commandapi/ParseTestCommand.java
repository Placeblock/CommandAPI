package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
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
            .literal("add", addCommand -> addCommand
                .run(ctx -> {

                }))
            .literal("remove", addCommand -> addCommand
                .parameter("amount", new IntegerParameter<>(), amountParameter -> amountParameter
                    .run(ctx -> {

                    }))
        );
    }

    @Override
    public boolean hasPermission(String source, String permission) {
        return true;
    }

    @Override
    public void sendMessage(String source, TextComponent message) {

    }
}
