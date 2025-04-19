package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.message.CommandDesign;
import de.codelix.commandapi.core.message.CommandMessages;
import de.codelix.commandapi.core.parser.Source;
import de.codelix.commandapi.core.tree.Node;

import java.util.List;

public abstract class MinecraftDesign<M> extends CommandDesign<M> {

    public MinecraftDesign(CommandMessages<M> messages) {
        super(messages);
    }

    public abstract <S extends Source<M>> M getHelpMessage(Command<S, M, ?, ?, ?>  command, List<List<Node<S, M>>> branches);

}
