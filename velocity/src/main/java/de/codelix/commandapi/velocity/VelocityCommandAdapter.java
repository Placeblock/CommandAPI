package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import de.codelix.commandapi.core.design.CommandDesign;
import de.codelix.commandapi.minecraft.MCCommandAudience;
import net.kyori.adventure.text.TextComponent;

public abstract class VelocityCommandAdapter extends AbstractVelocityCommandAdapter<Player> {
    public VelocityCommandAdapter(String label, boolean async, CommandDesign commandDesign) {
        super(label, async, commandDesign);
    }

    @Override
    public boolean hasPermission(MCCommandAudience<Player> source, String permission) {
        return source instanceof ConsoleCommandSource || source.getPlayer().hasPermission(permission);
    }

    @Override
    public void sendMessage(MCCommandAudience<Player> source, TextComponent message) {
        source.sendMessage(message);
    }

    @Override
    public void sendMessageRaw(MCCommandAudience<Player> source, TextComponent message) {
        source.sendMessageRaw(message);
    }

    @Override
    public Player getCustomPlayer(MCCommandAudience<Player> source) {
        return source.getPlayer();
    }
}
