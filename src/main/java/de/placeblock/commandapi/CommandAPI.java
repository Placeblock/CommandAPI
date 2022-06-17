package de.placeblock.commandapi;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Console;
import java.util.List;

public final class CommandAPI extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        ScharkCommand<Player, Console> scharkCommand = new ScharkCommand<Player, Console>() {
            @Override
            public void onExecutePlayer(Player player, List<String> args) {

            }

            @Override
            public boolean hasPermission(Player player, String permission) {
                return false;
            }

            @Override
            public void sendHelpMessage(Player player) {

            }

            @Override
            public void sendNoPermMessage(Player player) {

            }
        }
    }

    @Override
    public void onDisable() {

    }
}
