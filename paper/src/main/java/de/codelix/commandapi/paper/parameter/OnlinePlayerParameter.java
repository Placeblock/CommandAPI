package de.codelix.commandapi.paper.parameter;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.paper.PaperSource;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class OnlinePlayerParameter<S extends PaperSource<?>> implements Parameter<Player, S, TextComponent> {
    @Override
    public Player parse(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equals(next)) {
                return onlinePlayer;
            }
        }
        throw new InvalidPlayerParseException(next);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S, TextComponent> ctx, ParsedCommand<S, TextComponent> cmd) {
        List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        return this.startsWith(names, ctx.getRemaining());
    }
}
