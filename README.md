# The CommandAPI

Builds are availible at the mavenCentral Repository 📦
<details>
<summary>Gradle Kotlin</summary>

```kotlin
implementation("de.codelix.commandapi:core:VERSION")
implementation("de.codelix.commandapi:paper:VERSION") //Paper-Bridge
implementation("de.codelix.commandapi:waterfall:VERSION") //Waterfall-Bridge
```
</details>
<details>
<summary>Gradle Groovy</summary>

```kotlin
implementation 'de.codelix.commandapi:core:VERSION'
implementation 'de.codelix.commandapi:paper:VERSION' //Paper-Bridge
implementation 'de.codelix.commandapi:waterfall:VERSION' //Waterfall-Bridge
```
</details>
<details>
<summary>Maven</summary>

```xml
<dependency>
    <groupId>de.codelix.commandapi</groupId>
    <artifactId>core</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>  <!--Paper-Bridge-->
    <groupId>de.codelix.commandapi</groupId>
    <artifactId>paper</artifactId>
    <version>VERSION</version>
</dependency>
<dependency> <!--Waterfall-Bridge-->
    <groupId>de.codelix.commandapi</groupId>
    <artifactId>waterfall</artifactId>
    <version>VERSION</version>
</dependency>
```
</details>

You can get the current version from [here](https://central.sonatype.com/search?q=de.codelix.commandapi)

## Introduction

Implementing commands in Spigot can be quite challenging 😟. 
As soon as they get more complex, things get complicated and unreadable quite fast 😟😟. 
This is what CommandAPI tries to solve 😀😀.  The goal is to create a structured and safe way to 
implement such commands and handle the execution of them!

## The CommandSource

A command gets always executed by a human (At least most of the time 🤖). This is the CommandSource.

## The parser

### Why do we need the parser?

The most important part of the CommandAPI is the parser. It basically takes a string as
an input and decides what should get executed or which error should be thrown.
As the parser is used quite often it should be as performant as possible.

Before the parser can evaluate any string, you have to give the parser instructions what should happen
in specific situations. These instructions are called the command-structure or command-tree and
 define, as the name already tells, the structure of the command.

Here is an example of such a command-tree. How you can implement them is described 
in [Command Creation](#command-creation).

/file save   ->   should safe the file<br>
/file append [line]   -> should append a line to the file<br>
/file load [file]    -> should load a file

The purpose of the parser is to evaluate strings like:<br>
"/file sa" -> Unknown Command<br>
"/file save" -> Call the method to save the file

This tree-structure is stored in the CommandNode class. It contains its children CommandNodes, the permission
required to access itself and the CommandExecutor, which is basically a lambda method, which is called when
this specific CommandNode is executed.

### How the parser works (In detail)

The parser evaluates the passed string from left to right. During the parsing process it gathers 
information like parsed parameters, parsing errors and so on. All this information is stored in the
ParsedCommand class, which can be accessed together with the CommandSource in the CommandExecutor to 
get parsed parameters and other information.

However, it could be possible, that there are multiple CommandExecutors that fit the passed string (Even ones,
that would return SyntaxExceptions). Therefore, the parser evaluates all possible branches, which means multiple 
ParsedCommands containing the information for the specific branches are returned. Based on the parsing errors 
contained in the ParsedCommands the best ParsedCommand gets executed, if there is any CommandExecutor.

The parser starts with index 0. At first, it will try, to parse the main literal. If that was successful, it sets
the cursor to right after the literal. Then it will try to parse the tree-structure recursively. If the parser stumbles
over a literal, it will try to parse the literal. If there is a parameter, it will call the parse() method of the Parameter.


## Command creation

Everything starts with the Command class. You can extend this class, but in [Paper and Waterfall in detail](#paper-and-waterfall-in-detail) 
we will discuss why there are better options. Weather you do it or not at some point you have to implement the generateCommand
method which is called right after instantiating the command. There is one parameter, which is the base-literal CommandNodeBuilder 
you can build upon. At the end the complete tree-structure has to be returned for the command to work.

<details>
<summary>Example</summary>

This example creates a simple command with two sub-commands "add" and "remove".
Add has an enum parameter as a subcommand with no executor.
Remove has an integer parameter as a subcommand and prints the passed amount into the console on execute.
```java
public class CommandExample extends Command {
    //...
    @Override
    public LiteralCommandNodeBuilder<String> generateCommand(LiteralCommandNodeBuilder<String> builder) {
        return builder
            .then(
                literal("add").then(
                    parameter("material", enumparam(ExampleEnum.class, ExampleEnum.values()))
                )
            ).then(
                literal("remove").then(
                    parameter("amount", integer(0, 105))
                        .run((context, source) ->
                            System.out.println(context.getParsedParameter("amount"))))
            );
    }
    //...
}
```
</details>


### The tree structure

The base class for implementing the tree-structure is the LiteralCommandNode and the ParameterCommandNode. As the name describes,
LiteralCommandNode is for literals and ParameterCommandNode is for parameters. However, you actually do not use these classes at all.
There are corresponding builder classes, which simplify the implementation drastically. 

If you instantiate a new CommandNodeBuilder you have several options:
- .then -> adds a child commandNode to this commandNode
- .run -> specifies the CommandExecutor that should be executed when this commandNode gets parsed successfully and there is nothing more to parse.
- .withDescription -> specifies the description that should show up in the help message
- .withPermission -> specifies the permission needed to access this tree command

Particularly for Literals:
- .withAlias -> specifies an alias for the name

### Parameters

You have already learned that you use the ParameterCommandNode for specifying parameters. In the constructor of this class you have to 
provide the parameter that is used for this ParameterCommandNode. There are many ready-to-use parameters implemented:
- BooleanParameter
- DoubleParameter
- EnumParameter
- IntegerParameter
- StringOfListParameter
- StringParameter
- EnumParameter

If you want to add your own parameter you can do that by implementing the Parameter interface.
In the parse method you should throw a CommandParseException if the Parameter couldn't get parsed.

Accessing parsed parameters in the CommandExecutor is quite easy. As already described, all information gathered while parsing the command is
stored in the ParsedCommand instance, which is then passed to the CommandExecutor. Providing the name of the ParameterCommandNode you can access
parsed parameters there.

Attention: In the parse method of a parameter you have access to the ParsedCommand too, which means you can access already parsed parameters while
parsing another. This can be really helpful for tab-completing a list of elements from an object that is specified by another parameter.

## Design & Error Messages
Where are the messages??? This question is now to be answered! Maybe you have noticed that when you throw an exception in the parse method of your parameter,
you cannot specify an error-message anymore. The old version of the CommandAPI used to implement them directly into the parameters. However, because of this,
api-users couldn't set their own error-messages for already existing parameters. This is where the CommandDesign class flights directly into the screen. Every
Command you create has a CommandDesign attribute. If you don't set it via the constructor, it will use the global CommandDesign in the static field Command.DESIGN.
In this class you can register messages for specific exceptions. If the command throws an exception while parsing, it will get the according message
from the CommandDesign. 

<span>By default there are no error-messages registered</span>. 
<br>
To register the default messages use 
```java
new DefaultMessages().register(design)
```
The Implementations are named after the package-name + DefaultMessages:
- CoreDefaultMessages
- PaperDefaultMessages

By using registerDefaultMessages without providing a design it will register the messages directly to the global design.

## Paper and Waterfall in detail

### How to work with Paper and Waterfall

Initially the CommandAPI doesn't have anything to do with Minecraft. If you want to use the CommandAPI for Minecraft you
would have to implement the event handling and everything else all by yourself. This is why there are these to classes:
- AbstractPaperCommandBridge  ->  Handles events for Paper 
- AbstractWaterfallCommandBridge  ->  Handles events for Waterfall
These also provide unregister and register methods, which you have to call to access the command in-game.

But even if you use them you still have to implement methods like hasPermission() and sendMessage() yourself, as the
source is still generic and the API cannot know how to send a message to your source. For very minimalistic usage of
the API this is not simple enough. This is why there are two more classes:
- PaperCommandBridge  -> Extends AbstractPaperCommandBridge and implements hasPermission() and sendMessage()
- WaterfallCommandBridge  -> Extends AbstractWaterfallCommandBridge and implements hasPermission() and sendMessage()
However if you use these two you are forced to use the Paper Player or the Waterfall ProxiedPlayer as the source's player.

In conclusion, you can say that if you just want to create very simple commands you just extend one of these classes.

### The problem with generics

If you instantiate the tree-structure as described in 2. you will see quite fast that it's really annoying
to always specify the generics for the CommandSource. This is because Java's type-inferring does not work here.

#### How to avoid generics

However, when using Paper or Waterfall, you usually want to use the console as the sender. To make your life easier there
exist two classes called PaperCommandSource and WaterfallCommandSource, which do exactly that. This reduced the generics you
have to specify, but hasn't removed them entirely.
If you use the Paper Player class or the Waterfall ProxyPlayer class as the player of your source, you can use
- PaperCommandBridge.literal() -> for creating literals for Paper without generics
- PaperCommandBridge.parameter() -> for creating parameters for Paper without generics
- WaterfallCommandBridge.literal() -> for creating literals for Waterfall without generics
- WaterfallCommandBridge.parameter() -> for creating parameters for Waterfall without generics
It makes sense to put them in these classes, because if you use them to create a command you are forced to use the Paper Player 
and Waterfall ProxyPlayer too!

If you don't use these specific Player classes it's recommended to create such static methods for yourself to avoid
generics.
