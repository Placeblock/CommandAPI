package de.codelix.commandapi.paper.tree;

import de.codelix.commandapi.adventure.tree.AdventureArgument;
import de.codelix.commandapi.paper.PaperSource;
import org.bukkit.command.CommandSender;

public interface PaperArgument<T, S extends PaperSource<P>, P> extends PaperNode<S, P>, AdventureArgument<T, S, P, CommandSender> {
}
