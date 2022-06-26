# CommandAPI
API for an easier Command use on Minecraft Servers (BungeeCord, Spigot, ...).

### 🔴IMPORTANT🔴 Please read the whole documentation to prevent mistakes and to answer unanswered questions

CommandAPI simplifies and structures command definition and execution, especially with many subcommands.<br />
Auto-Tab-Completion can be a mess to implement in large commands. CommandAPI does it for you!<br />
It can be really hard to implement complex Commands, Help-Messages and Auto-Tab-Completion without many duplicated code snippets. Because the Structure of your commands and subcommands are stored only in one place, all of this can be done without much effort!

This API was originally inspired by Mojang's Brigadier, but in the end it turned out that i reworked nearly everything,
because for me Brigadier was unnecessary complex.
Bridges for Paper and Waterfall are included in the API.

## How does the API work?

Every command is stored in a tree-like structure <br />
Example:
```
literal
    literal2
        argument1
    literal3
        literal4
            argument2
        argument3
    argument4
        argument5
```
If you don't know what literals and arguments are, here is an example:
```
Literal   Literal   Argument
  \/        \/         \/
/party    invite    [player]
```
At the same time every single of them are Nodes.

### When using CommandAPI you can define this Structure by using the [ArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/ArgumentBuilder.java)
It is an abstract class and has the following methods:

<dl>
  <dt>then(ArgumentBuilder builder)</dt>
  <dd>Used to add Child ArgumentBuilder.</dd>
  <dt>executes(Command command)</dt>
  <dd>Sets the Callback to be executed when executing this node</dd>
  <dd>Command is a functional interface, so it can be used as a lambda function</dd>
  <dt>withDescription(TextComponent description)</dt>
  <dd>Sets the description of this node</dd>
  <dt>withPermission(String permission)</dt>
  <dd>Sets the permission that is required to run this command. </dd>
  <dt>requires(Predicate requirement)</dt>
  <dd>This Node is only executed if this Predicate returns true</dd>
</dl>

### There arer two implementations of the abstract [ArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/ArgumentBuilder.java)


#### [LiteralArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/LiteralArgumentBuilder.java) with the following additional methods:
<dl>
  <dt>withAlias(String alias)</dt>
  <dd>Adds an Alias to the Node.</dd>
</dl>

and the
#### [RequiredArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/RequiredArgumentBuilder.java) with the following additional methods:
<dl>
  <dt>suggests(Function&lt;String, List&lt;String&gt;&gt; customSuggestions)</dt>
  <dd>This Function will be called in addition to the ArgumentType when getting Suggestions for TabCompletion.</dd>
</dl>

####  You can use these two Builders to build your Command, or if you want to, define your own:
```
return new LiteralArgumentBuilder<Source>("fly")
    .executes(c -> {
        //Gets executed if somebody types /fly
    })
    .then(
        new RequiredArgumentBuilder<Source, ILobbyPlayer>("spieler", new LobbyPlayerArgumentType())
        .then(new RequiredArgumentBuilder<Source, Boolean>("true/false", new BooleanArgumentType())
            .executes(c -> {
                //Gets executed if somebody types /fly <spieler> 
            }))
    );
```

### What is source?
Since CommandAPI should be compatible with Spigot as well as Bungeecord the CommandAPI classes are generic.<br />
This means there is no fixed class for the Player or the Console, instead of this there is just the Source S.
When a command gets executed you get the source, another word would be "whoever typed in this command".


### Example for a "simple" Paper Command:
```
public class FlyCommand extends CommandAPICommand<PaperCommandSource<Player>> implements CommandExecutor, TabCompleter {

    public FlyCommand() {
        super("fly");
        Objects.requireNonNull(howeveryougetyourserver().getPluginCommand(label)).setExecutor(this);
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

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        String buffer = label + " " + String.join(" ", args);
        P customPlayer = null;
        if (sender instanceof Player player) {
            customPlayer = this.getCustomPlayer(player);
        }
        ParseResults<PaperCommandSource<P>> parseResults = this.parse(new PaperCommandSource<>(customPlayer, sender), buffer);
        return this.getSuggestions(parseResults);
    }
}
```
In Bungeecord things are even more complicated, because you can't extend CommandApiCommand as you have to extend Command.

WOHA, AND I HAVE TO DO THIS FOR EVERY SINGLE COMMAND? <br />
Long answer short: no, of course not. <br />
Since CommandAPI should be compatible with Spigot as well as Bungeecord the CommandAPI classes are generic.<br />
Because of this there are Methods like sendSourceMessage(Source source) or hasSourcePermission(Source source, String permission).<br />
These methods will be probably the same for all your commands, and you would have to implement these in all commands.<br />
One solution would be to create another class which extends CommandAPICommand and implements these methods.<br />
For Paper and Waterfall this is already done with the [WaterfallCommandBridge](src/main/java/de/placeblock/commandapi/bungee/WaterfallCommandBridge.java)
and [PaperCommandBridge](src/main/java/de/placeblock/commandapi/paper/PaperCommandBridge.java) classes.<br />
They still have some abstract methods you would have to implement in every command and it is still recommended to
create a class between the Paper-/Bungee-Commandbridge, an example is shown below.

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

### What is CustomPlayer?
When programming larger plugins you will likely have a Custom Player Class which holds the BukkitPlayer
and other important Data. To work with these Custom Players directly the WaterfallCommandSource which
holds the Player and the CommandSender, where Player is of the Type, which you pass to PaperCommandBridge.
When a command is executed you will receive the source and because of this generics you will get your
CustomPlayer directly.

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
            .withAlias("f")
    }
```


### You can add a Description to Commands:
```    
    @Override
    public LiteralArgumentBuilder<PaperCommandSource<Player>> generateCommand() {
        return new LiteralArgumentBuilder<PaperCommandSource<ILobbyPlayer>>("fly")
            .withDescription("Custom Description for This Command")
    }
```
The only use of descriptions is for generating the HelpMessage.
If no description is provided there will be no description in the HelpMessage.


### Generate HelpMessage
CommandAPI automatically sends HelpMessages, however, if you want just to generate the Help Message, here you go:
```
PaperCommand command = somePaperCommand()
TextComponent helpMessage = command.generateHelpMessage(source)
```
TextComponent from [AdventureAPI](https://github.com/KyoriPowered/adventure) is returned
