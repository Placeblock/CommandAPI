package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.message.CommandDesign;
import de.codelix.commandapi.core.message.CommandMessages;

public abstract class MinecraftDesign<S extends MinecraftSource<?, ?, M>, M> extends CommandDesign<M> {

    public MinecraftDesign(CommandMessages<M> messages) {
        super(messages);
    }

    public abstract M getHelpMessage(Command<S, M, ?, ?, ?>  command, S source);

}
