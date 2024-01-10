package de.codelix.commandapi.minecraft;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;

public class MinecraftCommandAudience<P extends Audience> extends CommandAudience {
    public MinecraftCommandAudience(Audience sourceAudience, TextComponent prefix) {
        super(sourceAudience, prefix);
    }

    public P getPlayer() {
        //noinspection unchecked
        return (P) this.sourceAudience;
    }

}
