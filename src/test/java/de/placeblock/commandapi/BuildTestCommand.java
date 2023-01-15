package de.placeblock.commandapi;

import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parameter.IntegerParameter;
import de.placeblock.commandapi.core.tree.builder.LiteralTreeCommandBuilder;
import de.placeblock.commandapi.core.tree.builder.TreeCommandBuilder;
import net.kyori.adventure.text.TextComponent;

/**
 * Author: Placeblock
 */
public class BuildTestCommand extends Command<String> {
    public BuildTestCommand() {
        super();
    }

    @Override
    public TreeCommandBuilder<String> getCommandBuilder() {
        return new LiteralTreeCommandBuilder<String>("party")
            .parameter("amount", new IntegerParameter<>(), amountParameter -> {});
    }

    @Override
    public boolean hasPermission(String source, String permission) {
        return false;
    }

    @Override
    public void sendMessage(String source, TextComponent message) {

    }
}
