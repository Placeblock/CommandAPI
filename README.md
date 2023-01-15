# CommandAPI (< 2.0.0)
API for an easier Command use on Minecraft Servers (BungeeCord, Spigot, ...).

### 🔴IMPORTANT🔴 Please read the whole documentation to prevent mistakes and to answer unanswered questions

CommandAPI simplifies and structures subCommand definition and execution, especially with many subcommands.<br />
Auto-Tab-Completion can be a mess to implement in large commands. CommandAPI does it for you!<br />
It can be really hard to implement complex Commands, Help-Messages and Auto-Tab-Completion without many duplicated code snippets. Because the Structure of your commands and subcommands are stored only in one place, all of this can be done without much effort!

This API was originally inspired by Mojang's Brigadier, but in the end it turned out that I reworked nearly everything,
because for me Brigadier was unnecessary complex.
Bridges for Paper and Waterfall are included in the API.

# Documentation

Every subCommand is stored in a tree-like structure <br />
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

## How to create the Command Structure
#### When using CommandAPI you can define this Structure by using the [ArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/ArgumentBuilder.java)
It is an abstract class and has the following methods:

<dl>
  <dt>then(ArgumentBuilder builder)</dt>
  <dd>Used to add Child ArgumentBuilder.</dd>
  <dt>executes(Command subCommand)</dt>
  <dd>Sets the Callback to be executed when executing this node.</dd>
  <dd>Command is a functional interface, so it can be used as a lambda function.</dd>
  <dt>withDescription(TextComponent description)</dt>
  <dd>Sets the description of this node.</dd>
  <dd>The only use of descriptions is for generating the HelpMessage.</dd>
  <dd>If no description is provided there will be no description in the HelpMessage.</dd>

  <dt>withPermission(String permission)</dt>
  <dd>Sets the permission that is required to run this subCommand. </dd>
  <dt>requires(Predicate requirement)</dt>
  <dd>This Node is only executed if this Predicate returns true.</dd>
</dl>

### There arer two implementations of the abstract [ArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/ArgumentBuilder.java)


#### [LiteralArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/LiteralArgumentBuilder.java) with the following additional methods:
Used for Literals (above-mentioned)
<dl>
  <dt>withAlias(String alias)</dt>
  <dd>Adds an Alias to the Node.</dd>
</dl>


#### [RequiredArgumentBuilder](src/main/java/de/placeblock/commandapi/core/builder/RequiredArgumentBuilder.java) with the following additional methods:
Used for Arguments (above-mentioned)
<dl>
  <dt>suggests(Function&lt;String, List&lt;String&gt;&gt; customSuggestions)</dt>
  <dd>This Function will be called in addition to the ArgumentType when getting Suggestions for TabCompletion.</dd>
</dl>
RequiredArgumentBuilder needs an ArgumentType, at example, <br />
/msg [player] [msg] <- This is an Argument of Type String
When the Player types a subCommand, the parser tries to parse arguments into their Types.
You can create your own CustomArgumentType by implementing ArgumentType.

###  You can use these two Builders to build your Command, or if you want to, define your own:
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

## What is source?
Since CommandAPI should be compatible with Paper as well as Waterfall the CommandAPI classes are generic.<br />
This means there is no fixed class for the Player or the Console, instead of this there is just the Source S.
When a subCommand gets executed you get the source, another word would be "whoever typed in this subCommand".

You should create a Source Class which holds your Custom Player and the CommandSender,
because when the subCommand gets executed from console you can set the Custom Player to null and
in your execute lambda a simple check is needed to confirm that the subCommand was sent by an actual player. <br />
IMPORTANT: PaperCommandBridge and WaterfallCommandBridge does this for you (More information further down).

## Example for a "simple" Paper Command without implementation:
```
public class FlyCommand extends CommandAPICommand<Source> implements CommandExecutor, TabCompleter {

    public FlyCommand() {
        super("fly");
        //Registration missing
    }

    @Override
    public LiteralArgumentBuilder<Source> generateCommand() {
        //Here you can Define your Command Structure
    }

    @Override
    public boolean hasSourcePermission(Source source, String permission) {
        //Implementation missing
    }

    @Override
    public void sendSourceMessage(Source source, TextComponent textComponent) {

    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command subCommand, @NotNull String label, @NotNull String[] args) {
        //Implementation missing
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command subCommand, @NotNull String label, @NotNull String[] args) {
        //Implementation missing
    }
}
```
The CommandAPI does not know how to send Messages to the Source because it doesn't know of which type Source is.<br />
This is why you have to implement methods Like sendSourceMessage(Source source, TextComponent textComponent).<br />

Maybe you are wondering if you have to do this for every single subCommand...
Long answer short: no, of course not. <br />
These methods will be probably the same for all your commands, and it would be stupid to implement them again and again.<br />
One solution would be to create another class which extends CommandAPICommand and implements these methods.<br />
For Paper and Waterfall this is already done with the [WaterfallCommandBridge](src/main/java/de/placeblock/commandapi/bungee/WaterfallCommandBridge.java)
and [PaperCommandBridge](src/main/java/de/placeblock/commandapi/paper/PaperCommandBridge.java) classes.<br />
They already have implemented Command Execution, Tab Completion and Command Registration,
but still have some abstract methods (explained below example) you would have to implement in every subCommand, however they don't need
as much logic as the above-mentioned. <br />
Because there are still methods which are the same for every subCommand, 
it is recommended to create a class between the Paper-/Bungee-Commandbridge, an example is shown below.

### Example (Paper):
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
    protected boolean hasPermission(CustomPlayer customPlayer, String permission) {
        return CheckIfYourCustomPlayerHasPermission(permission)
    }

    @Override
    protected void sendMessage(CustomPlayer customPlayer, TextComponent textComponent) {
        customPlayer.getBukkitPlayer().sendMessage(textComponent);
    }
}
```
This looks much cleaner then the first-mentioned class, and now you can use PaperCommand freely.

### What is CustomPlayer?
Remember that I said that Paper-/Waterfall-CommandBridge handle the
Source for you? Well, it isn't 100% right. I could have implemented a Source Class just with
(for paper) the Bukkit Player and the CommandSender, and (for Waterfall) the ProxiedPlayer and the CommandSender,
but in larger plugins you will likely have a Custom Player which holds your player data and (with Paper) your Bukkit Player.
To Support these Custom Players, the Source used in the Paper-/Waterfall-CommandBridge is generic. You can set the Type
for the Player to your Custom Player!
But because the API don't know the type of Player, it doesn't know how to check Permissions, send Messages, etc.
This is why you have to implement Methods like sendMessage(), hasPermission() etc.


## Generate HelpMessage
CommandAPI automatically sends HelpMessages, however, if you want just to generate the Help Message, here you go:
```
CommandAPICommand subCommand = someCommandAPICommand()
TextComponent helpMessage = subCommand.generateHelpMessage(source)
```
TextComponent from [AdventureAPI](https://github.com/KyoriPowered/adventure) is returned
