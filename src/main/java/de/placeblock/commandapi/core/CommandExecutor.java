package de.placeblock.commandapi.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Author: Placeblock
 */
@Getter
@RequiredArgsConstructor
public class CommandExecutor<C, P> {
    private final C console;
    private final P player;

    public boolean isPlayer() {
        return this.player != null;
    }
}
