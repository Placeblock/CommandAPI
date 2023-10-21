package de.codelix.commandapi.minecraft;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;

public class MCCommandAudience<P> extends CommandAudience {
    public MCCommandAudience(Audience sourceAudience, TextComponent prefix) {
        super(sourceAudience, prefix);
    }

    public P getPlayer() {
        //noinspection unchecked
        return (P) this.sourceAudience;
    }

}
