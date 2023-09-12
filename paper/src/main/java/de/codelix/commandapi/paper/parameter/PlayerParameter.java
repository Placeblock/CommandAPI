package de.codelix.commandapi.paper.parameter;

import de.codelix.commandapi.core.SuggestionBuilder;
import de.codelix.commandapi.core.exception.CommandParseException;
import de.codelix.commandapi.core.parameter.Parameter;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;
import de.codelix.commandapi.paper.exception.InvalidPlayerException;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PlayerParameter<S> implements Parameter<S, Player> {
    private final Supplier<List<Player>> players;

    @SuppressWarnings("unused")
    public static <S> PlayerParameter<S> player(Supplier<List<Player>> players) {
        return new PlayerParameter<>(players);
    }

    @Override
    public Player parse(ParsedCommandBranch<S> command, S source) throws CommandParseException {
        String word = command.getReader().readUnquotedString();
        for (Player player : this.players.get()) {
            if (player.getName().equals(word)) {
                return player;
            }
        }
        throw new InvalidPlayerException(word);
    }

    @Override
    public void getSuggestions(SuggestionBuilder<S> suggestionBuilder) {
        String partial = suggestionBuilder.getRemaining();
        List<String> names = this.players.get().stream().map(Player::getName).toList();
        suggestionBuilder.withSuggestions(Parameter.startsWith(names, partial));
    }
}
