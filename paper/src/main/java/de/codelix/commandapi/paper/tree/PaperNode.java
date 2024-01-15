package de.codelix.commandapi.paper.tree;

import de.codelix.commandapi.adventure.tree.AdventureNode;
import de.codelix.commandapi.paper.PaperSource;
import org.bukkit.command.CommandSender;

public interface PaperNode<S extends PaperSource<P>, P> extends AdventureNode<S, P, CommandSender> {
}
