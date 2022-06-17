# CommandAPI
API for an easier Command use in Waterfall / Paper , Help-Message and Auto-Completion support

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
If you want to create multiple Commands is recommended to create a class which extends ScharkCommand and implements theese methods.

#### Example (BungeeCord):
```
public abstract class BungeeCommand extends ScharkCommand<ProxiedPlayer, CommandSender> {

    public BungeeCommand(String label, String description, String permission, boolean showWithoutSubcommands) {
        super(label, description, permission, showWithoutSubcommands);
    }

    @Override
    public boolean hasPermission(ProxiedPlayer player, String permission) {
        return CorePermission.hasPermission(player, permission);
    }

    @Override
    public void sendHelpMessage(ProxiedPlayer player) {
        player.sendMessage(this.generateHelp());
    }

    @Override
    public void sendNoPermMessage(ProxiedPlayer player) {
        player.sendMessage(CoreMessages.INVALID_COMMAND);
    }
}
```

Now you only have to implement onExecutePlayer in your command.

### What is showWithoutSubCommands?

Example Command Structure
```
command [arg1] [arg2]
    sub2 [arg3]
    sub3
```
If you set showWithoutSubCommands in sub1 to false the HelpMessage will look like this:
```
/command [arg1] [arg2] sub2 [arg3]
/command [arg1] [arg2] sub3
```
If you set showWithoutSubCommands in sub1 to true the HelpMessage will look like this:
```
/command [arg1] [arg2]
/command [arg1] [arg2] sub2 [arg3]
/command [arg1] [arg2] sub3
```
It does what it says, it shows the command without its subcommands!


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
  scharkCommand.addArguments("arg2", "arg3", "arg4");
```

Theese methods are chainable, still it is not recommended in complex commands because of readability

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

If an user executes th following command
/command sub1 [arg1] sub2 [arg2] [arg3]
The subCommand "sub2" will be called again, but with the arguments including [arg1] from the subCommand "sub1", because maybe [arg1] is important for sub2

```
  ScharkCommand scharkCommand = ...some complex scharkCommand structure
  scharkCommand.execute(player, console, args, new ArrayList<>());
```
The empty list needs to be passed as the last argument because of the recursion behind the method.
