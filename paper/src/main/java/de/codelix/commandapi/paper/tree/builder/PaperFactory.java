package de.codelix.commandapi.paper.tree.builder;

import de.codelix.commandapi.adventure.AdventureFactory;
import de.codelix.commandapi.paper.PaperSource;
import org.bukkit.command.CommandSender;

public interface PaperFactory<L extends PaperLiteralBuilder<?, ?, S, P>, A extends PaperArgumentBuilder<?, ?, ?, S, P>, S extends PaperSource<P>, P> extends AdventureFactory<L, A, S, P, CommandSender> {
}
