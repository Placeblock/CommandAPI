package de.codelix.commandapi.paper;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class DefaultPaperSource extends PaperSource<Player> {
    public DefaultPaperSource(Player player, CommandSender console) {
        super(player, console);
    }

    @Override
    public void sendMessagePlayer(TextComponent message) {
        this.getPlayer().sendMessage(message);
    }

    @Override
    public boolean hasPermissionPlayer(String permission) {
        return this.getPlayer().hasPermission(permission);
    }
}
