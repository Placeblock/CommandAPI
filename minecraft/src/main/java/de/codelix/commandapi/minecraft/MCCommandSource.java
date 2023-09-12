package de.codelix.commandapi.minecraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class MCCommandSource<P, C> {
    private final P player;
    private final C console;

    public boolean isPlayer() {
        return player != null;
    }
}
