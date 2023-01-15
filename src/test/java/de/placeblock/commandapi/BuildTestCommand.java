package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public class BuildTestCommand extends Command<String> {

    public BuildTestCommand() {
        super("testcommandbuild");
    }

    @Override
    public LiteralTreeCommandBuilder<String> generateCommand(LiteralTreeCommandBuilder<String> builder) {
        return builder.parameter("amount", new IntegerParameter<>(), amountParameter -> {});
    }

    @Override
    public boolean hasPermission(String source, String permission) {
        return true;
    }

    @Override
    public void sendMessage(String source, TextComponent message) {

    }
}
