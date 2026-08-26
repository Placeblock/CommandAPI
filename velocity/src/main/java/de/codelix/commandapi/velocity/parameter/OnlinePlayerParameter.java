package de.codelix.commandapi.velocity.parameter;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.minecraft.exception.InvalidPlayerParseException;
import de.codelix.commandapi.velocity.VelocitySource;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.List;

@RequiredArgsConstructor
public class OnlinePlayerParameter<S extends VelocitySource<?>> implements Parameter<Player, S, Component> {
    private final ProxyServer proxy;
    @Override
    public Player parse(ParseContext<S, Component> ctx, ParsedCommand<S, Component> cmd) throws ParseException {
        String next = ctx.getInput().poll();
        for (Player player : this.proxy.getAllPlayers()) {
            if (player.getUsername().equals(next)) {
                return player;
            }
        }
        throw new InvalidPlayerParseException(next);
    }

    @Override
    public List<String> getSuggestions(ParseContext<S, Component> ctx, ParsedCommand<S, Component> cmd) {
        List<String> names = this.proxy.getAllPlayers().stream().map(Player::getUsername).toList();
        return this.startsWith(names, ctx.getRemaining());
    }
}
