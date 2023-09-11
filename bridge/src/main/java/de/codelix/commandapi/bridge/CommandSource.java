package de.codelix.commandapi.bridge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class CommandSource<P, C> {
    private final P player;
    private final C sender;

    public boolean isPlayer() {
        return player != null;
    }
}
