# CommandAPI
API for an easier Command use on Minecraft Servers (BungeeCord, Spigot, ...).

### 🔴IMPORTANT🔴 Please read the whole documentation to prevent mistakes and to answer unanswered questions

CommandAPI simplifies and structures command definition and execution, especially with many subcommands.<br />
Auto-Tab-Completion can be a mess to implement in large commands. CommandAPI does it for you!<br />
It can be really hard to implement complex Commands, Help-Messages and Auto-Tab-Completion without many duplicated code snippets. Because the Structure of your commands and subcommands are stored only in one place, all of this can be done without much effort!


### Example for a simple Command:
```
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
```

Because CommandAPI should be compatible with Spigot as well as Bungeecord the Player class and Console class are generic. Because of this there
are Methods like sendHelpMessage(Player player) or hasPermission(Player player, String permission).
If you want to create multiple Commands it is recommended to create a class which extends ScharkCommand and implements theese methods, because they are the same for all commands.

#### Example (BungeeCord):
```
public abstract class BungeeCommand extends ScharkCommand<ProxiedPlayer, CommandSender> {

    public BungeeCommand(String label, String description, String permission, boolean showWithoutSubcommands) {
        super(label, description, permission, showWithoutSubcommands);
    }

    @Override
    public boolean hasPermission(ProxiedPlayer player, String permission) {
        return player.hasPermission(player, permission);
    }

    @Override
    public void sendHelpMessage(ProxiedPlayer player) {
        player.sendMessage(this.generateHelp());
    }

    @Override
    public void sendNoPermMessage(ProxiedPlayer player) {
        player.sendMessage(Messages.NO_PERM);
    }
}
```

Now you only have to implement onExecutePlayer in your command.

### What is showWithoutSubCommands?

Example Command Structure
```
command
    sub2 [arg3]
    sub3
```
If you set showWithoutSubCommands in sub1 to false the HelpMessage will look like this:
```
/command sub2 [arg3]
/command sub3
```
If you set showWithoutSubCommands in sub1 to true the HelpMessage will look like this:
```
/command 
/command sub2 [arg3]
/command sub3
```
It does what it says, it shows the command without its subcommands!
<br></br>
<br></br>
## Features

### You can create complex Command Structures:
```
ScharkCommand scharkCommand = ...
ScharkCommand scharkCommand2 = ...
scharkCommand.addSubCommand(scharkCommand2);
```

### You can add Aliases to Commands:
```
ScharkCommand scharkCommand = ...
scharkCommand.addAlias("alias1");
scharkCommand.addAliases("alias2", "alias3", "alias4");
```

### You can add Arguments to Commands:
```
ScharkCommand scharkCommand = ...
scharkCommand.addArgument("arg1");
scharkCommand.addOptionalArgument("arg1");
scharkCommand.addArguments("arg2", "arg3", "arg4");
scharkCommand.addOptionalArguments("arg1", "arg3", "arg10");
```

Theese methods are chainable, still it is not recommended in complex commands because of readability

### Add Extra Tab-Completions
Sometimes you want to add extra elements to Auto-Completion, at example if you want to invite somebody to your party, all online friends should be autocompleted.
CommandAPI has a way to implement this easily!
```
    partyCommand.addSubCommand(new BungeeCommand("invite", "Invite Players to your Party", "network.party.invite", false) {
        @Override
        public void onExecutePlayer(ProxiedPlayer player, List<String> args) {
            //Invite args[0] to player's Party
        }
        @Override
        public Set<String> getExtraTabCompletions(ProxiedPlayer player, int argindex) {
            return SomeFriendManager.getFriends(player);
        }
    }.addArgument("player")
```
❗IMPORTANT❗
You don't have to filter Elements that match with the beginning of the user input, CommandAPI does this for you ;)
If you have multiple Arguments argindex indicates which argument is going to be tab-completed.

### Generate HelpMessage
```
ScharkCommand scharkCommand = ...
TextComponent helpMessage = scharkCommand.generateHelpMessage();
```
TextComponent from [AdventureAPI](https://github.com/KyoriPowered/adventure) is returned

### Execute Command

Every ScharkCommand can be executed. CommandAPI will automatically call subCommands and pass arguments to the onExecute methods of them.
If an user executes the following command
/command sub1 sub2 [arg1] [arg2] 
The onExecute method of the subCommand labeled "sub2" will be called with the arguments [arg1, arg2]
```
ScharkCommand scharkCommand = ...some complex scharkCommand structure
scharkCommand.execute(player, console, args, new ArrayList<>());
```
<br></br>
<br></br>
## How to connect CommandAPI with Spigot or BungeeCord?
### Example for BungeeCord:
```
public abstract class CoreCommand extends Command implements TabExecutor {

    protected ScharkCommand<CorePlayer, CommandSender> scharkCommand;

    public CoreCommand(String label, String[] aliases) {
        super(label, null, aliases);
        this.scharkCommand = this.generateBungeeCommand(label, aliases);
    }

    public abstract BungeeCommand generateBungeeCommand(String label, String[] aliases);

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        this.scharkCommand.execute(
                commandSender instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer : null,
                commandSender,
                List.of(args), new ArrayList<>());
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer p)) return ImmutableSet.of();
        return this.scharkCommand.getTabCompletions(p, args);
    }
}
```
You can use this class for all your other commands.
Now you can just create at example a PartyCommand class which extends CoreCommand and register it in your Main Class!

