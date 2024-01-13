package de.codelix.commandapi.minecraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MinecraftSource<P, C> {
    private final P player;
    private final C console;

    public boolean isPlayer() {
        return this.player != null;
    }
    public boolean isConsole() {
        return this.console != null;
    }
}
