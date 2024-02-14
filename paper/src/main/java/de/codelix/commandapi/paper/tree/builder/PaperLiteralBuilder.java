package de.codelix.commandapi.paper.tree.builder;

import de.codelix.commandapi.adventure.tree.builder.AdventureLiteralBuilder;
import de.codelix.commandapi.paper.PaperSource;
import de.codelix.commandapi.paper.tree.PaperLiteral;
import org.bukkit.command.CommandSender;

public interface PaperLiteralBuilder<B extends PaperLiteralBuilder<B, R, S, P>, R extends PaperLiteral<S, P>, S extends PaperSource<P>, P> extends PaperNodeBuilder<B, R, S, P>, AdventureLiteralBuilder<B, R, S, P, CommandSender> {
}
