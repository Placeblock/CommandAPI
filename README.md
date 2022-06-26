# CommandAPI
API for an easier Command use on Minecraft Servers (BungeeCord, Spigot, ...).

### 🔴IMPORTANT🔴 Please read the whole documentation to prevent mistakes and to answer unanswered questions

CommandAPI simplifies and structures command definition and execution, especially with many subcommands.<br />
Auto-Tab-Completion can be a mess to implement in large commands. CommandAPI does it for you!<br />
It can be really hard to implement complex Commands, Help-Messages and Auto-Tab-Completion without many duplicated code snippets. Because the Structure of your commands and subcommands are stored only in one place, all of this can be done without much effort!

This API was originally inspired by Mojang's Brigadier, but in the end it turned out that i reworked nearly everything,
because for me Brigadier was unnecessary complex.
Bridges for Paper and Waterfall are included in the API.

### Example for a "simple" Paper Command:
```
public class FlyCommand extends CommandAPICommand<PaperCommandSource<Player>> implements CommandExecutor, Listener {

    public FlyCommand() {
        super("fly");
    }

    @Override
    public LiteralArgumentBuilder<PaperCommandSource<Player>> generateCommand() {
        return new LiteralArgumentBuilder<PaperCommandSource<Player>>("fly")
            .executes(c -> {
                Player player = c.getSource().getPlayer();
                //FLY LOGIC
            });
    }

    @Override
    public boolean hasSourcePermission(PaperCommandSource<Player> source, String permission) {
        if (source.getPlayer() != null) {
            return source.getPlayer().hasPermission(permission);
        } else {
            return true;
        }
    }

    @Override
    public void sendSourceMessage(PaperCommandSource<Player> source, TextComponent textComponent) {
        source.getSender().sendMessage(textComponent);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        P lobbyPlayer = null;
        if (sender instanceof Player player) {
            lobbyPlayer = this.getCustomPlayer(player);
        }
        PaperCommandSource<P> source = new PaperCommandSource<>(lobbyPlayer, sender);
        ParseResults<PaperCommandSource<P>> parseResults = this.parse(source, label + " " + String.join(" ", args));
        this.execute(parseResults);
        return true;
    }

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer().substring(1);
        if (!buffer.split(" ")[0].equals(this.getName())) {
            return;
        }
        P customPlayer = null;
        CommandSender sender = event.getSender();
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<PaperCommandSource<P>> parseResults = this.parse(new PaperCommandSource<>(customPlayer, sender), buffer);
        this.getSuggestions(parseResults).thenAccept(suggestions -> {
            event.setCompletions(suggestions);
            event.setHandled(true);
        }).exceptionally(throwable -> {
            event.setCompletions(new ArrayList<>());
            event.setHandled(true);
            return null;
        });
    }
    
}
```

WOHA, AND I HAVE TO DO THIS FOR EVERY SINGLE COMMAND?

Since CommandAPI should be compatible with Spigot as well as Bungeecord the CommandAPI classes are generic.
Because of this there are Methods like sendSourceMessage(Source source) or hasSourcePermission(Source source, String permission).
These methods will be probably the same for all your commands, and you would have to implement these in all commands.
One solution would be to create another class which extends CommandAPICommand and implements these methods.
For Paper and Waterfall this is already done by the classes BungeeCommandBridge and PaperCommandBridge.
They still have some abstract methods you would have to implement in every command and it is still recommended to
create a class between the Paper-/Bungee-Commandbridge, an exmample is shown below.

#### Example (Paper):
```
public abstract class PaperCommand extends PaperCommandBridge<CustomPlayer> {
    public PaperCommand(String label) {
        super(label);
    }

    @Override
    public LobbySlime getPlugin() {
        return MainPluginClass.getInstance();
    }

    @Override
    protected CustomPlayer getCustomPlayer(Player player) {
        return SomeMethodToGetYourCustomPlayerFromBukkitPlayer(player);
    }

    @Override
    protected boolean hasPermission(CustomPlayer lobbyPlayer, String permission) {
        return CheckIfYourCustomPlayerHasPermission(permission)
    }

    @Override
    protected void sendMessage(CustomPlayer customPlayer, TextComponent textComponent) {
        customPlayer.getBukkitPlayer().sendMessage(textComponent);
    }
}
```

Now you can use PaperCommand freely.


## Features

### You can create complex Command Structures:
```
    @Override
    public LiteralArgumentBuilder<PaperCommandSource<Player>> generateCommand() {
        return new LiteralArgumentBuilder<PaperCommandSource<ILobbyPlayer>>("fly")
            .executes(c -> {
                //Gets executed if a player types /fly
            })
            .then(
                new RequiredArgumentBuilder<PaperCommandSource<ILobbyPlayer>, ILobbyPlayer>("spieler", new LobbyPlayerArgumentType())
                .then(new RequiredArgumentBuilder<PaperCommandSource<ILobbyPlayer>, Boolean>("true/false", new BooleanArgumentType())
                    .executes(c -> {
                        //Gets executed if a player types /fly <spieler> 
                    }))
            );
    }
```
Use LiteralArgumentBuilder for SubCommands and RequiredArgumentBuilder for Parameters a player has to fulfill.

### You can add Aliases to Commands:
```    
    @Override
    public LiteralArgumentBuilder<PaperCommandSource<Player>> generateCommand() {
        return new LiteralArgumentBuilder<PaperCommandSource<ILobbyPlayer>>("fly")
            .withAlias("flymode")
    }
```


### Generate HelpMessage
CommandAPI automatically sends HelpMessages, however, if you want just to generate the Help Message, here you go:
```
PaperCommand command = somePaperCommand()
TextComponent helpMessage = command.generateHelpMessage(source)
```
TextComponent from [AdventureAPI](https://github.com/KyoriPowered/adventure) is returned
