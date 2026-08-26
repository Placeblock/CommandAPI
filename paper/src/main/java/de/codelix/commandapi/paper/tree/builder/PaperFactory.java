package de.codelix.commandapi.paper.tree.builder;

import de.codelix.commandapi.adventure.AdventureFactory;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.paper.PaperSource;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface PaperFactory<S extends PaperSource<P>, P> extends AdventureFactory<S, P, CommandSender> {
    PaperLiteralBuilder<?, ?, S, P> literal(String name, String... aliases);

    <T> PaperArgumentBuilder<T, ?, ?, S, P> argument(String name, Parameter<T, S, Component> parameter);
}
