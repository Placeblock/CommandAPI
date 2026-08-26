package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import de.codelix.commandapi.adventure.AdventureSource;
import net.kyori.adventure.text.Component;

public abstract class VelocitySource<P> extends AdventureSource<P, ConsoleCommandSource> {
    @Override
    public void sendMessageConsole(Component message) {
        this.getConsole().sendMessage(message);
    }

    public VelocitySource(P player, ConsoleCommandSource console) {
        super(player, console);
    }
}
