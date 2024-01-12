package de.codelix.commandapi.minecraft;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public record MinecraftSource<P, C>(P player, C console) {
    public boolean isPlayer() {
        return this.player != null;
    }
    public boolean isConsole() {
        return this.console != null;
    }
}
