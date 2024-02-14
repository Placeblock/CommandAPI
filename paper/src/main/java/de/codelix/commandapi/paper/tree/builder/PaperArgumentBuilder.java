package de.codelix.commandapi.paper.tree.builder;

import de.codelix.commandapi.adventure.tree.builder.AdventureArgumentBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperArgument;
import org.bukkit.command.CommandSender;

public interface PaperArgumentBuilder<T, B extends PaperArgumentBuilder<T, B, R, S, P>, R extends PaperArgument<T, S, P>, S extends PaperSource<P>, P> extends PaperNodeBuilder<B, R, S, P>, AdventureArgumentBuilder<T, B, R, S, P, CommandSender> {
}
