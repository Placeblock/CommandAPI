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

    @Override
    public void sendMessage(M message) {
        if (this.isConsole()) {
            this.sendMessageConsole(message);
        } else {
            this.sendMessagePlayer(message);
        }
    }

    @Override
    public boolean hasPermission(String permission) {
        if (this.isConsole()) return true;
        return this.hasPermissionPlayer(permission);
    }

    public abstract boolean hasPermissionPlayer(String permission);
    public abstract void sendMessagePlayer(M message);
    public abstract void sendMessageConsole(M message);
}
