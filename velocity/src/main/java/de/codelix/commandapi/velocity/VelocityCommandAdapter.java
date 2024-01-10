package de.codelix.commandapi.velocity;

import com.velocitypowered.api.proxy.connection.Player;
import de.codelix.commandapi.core.design.CommandDesign;
import net.kyori.adventure.text.TextComponent;

public abstract class VelocityCommandAdapter extends AbstractVelocityCommandAdapter<Player> {
    public VelocityCommandAdapter(String label) {
        super(label);
    }

    public VelocityCommandAdapter(String label, CommandDesign commandDesign) {
        super(label, commandDesign);
    }

    public VelocityCommandAdapter(String label, boolean async) {
        super(label, async);
    }

    public VelocityCommandAdapter(String label, boolean async, CommandDesign commandDesign) {
        super(label, async, commandDesign);
    }

    @Override
    public Player getCustomPlayer(Player defaultPlayer) {
        return defaultPlayer;
    }

    @Override
    public void sendMessagePlayer(Player customPlayer, TextComponent message) {
        customPlayer.sendMessage(message);
    }

    @Override
    public boolean hasPermissionPlayer(Player customPlayer, String permission) {
        return customPlayer.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(VelocityCommandSource<Player> source, String permission) {
        return !source.isPlayer()||source.getPlayer().hasPermission(permission);
    }

    @Override
    public void sendMessageRaw(VelocityCommandSource<Player> source, TextComponent message) {
        if (source.isPlayer()) {
            source.getPlayer().sendMessage(message);
        } else {
            source.getConsole().sendMessage(message);
        }
    }
}
