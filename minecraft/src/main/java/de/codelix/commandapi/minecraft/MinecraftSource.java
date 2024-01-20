package de.codelix.commandapi.minecraft;

import de.codelix.commandapi.core.parser.Source;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class MinecraftSource<P, C, M> implements Source<M> {
    private final P player;
    private final C console;

    public boolean isPlayer() {
        return this.player != null;
    }
    public boolean isConsole() {
        return this.console != null;
    }
}
