package de.codelix.commandapi.paper.tree;

import de.codelix.commandapi.adventure.tree.AdventureLiteral;
import de.codelix.commandapi.paper.PaperSource;
import org.bukkit.command.CommandSender;

public interface PaperLiteral<S extends PaperSource<P>, P> extends PaperNode<S, P>, AdventureLiteral<S, P, CommandSender> {
}
