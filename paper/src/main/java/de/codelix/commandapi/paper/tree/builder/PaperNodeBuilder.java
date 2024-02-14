package de.codelix.commandapi.paper.tree.builder;

import de.codelix.commandapi.adventure.tree.builder.AdventureNodeBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperNode;
import org.bukkit.command.CommandSender;

public interface PaperNodeBuilder<B extends PaperNodeBuilder<B, R, S, P>, R extends PaperNode<S, P>, S extends PaperSource<P>, P> extends AdventureNodeBuilder<B, R, S, P, CommandSender> {
}
