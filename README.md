# Builds

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

# The CommandAPI

CommandAPI is a generic and powerful API which covers all requirements that come with command building.
Whether you want to use it for the command line, for minecraft servers or other applications, it just works!

### Command Structure
A typical CommandAPI command looks like this. Other CommandAPIs usually contain
a lot of generics which harms readability. Here the generics are completely hidden
from the developer!

Here is an example for a command.
```java
builder
.then(this.factory().literal("test")
    .then(this.factory().argument("name", new WordParameter<>())
        .run((String source, String name) -> {
            
        })
    )
)
```
The syntax does not matter at the moment. For now the goal is to understand the basic concepts!
Every command consists of nodes. Several Nodes connected create the tree structure
of the command.
There are different types of Nodes included in the API.

#### 1. Literals
A Literal is just a word which has to be typed. A command to add/remove fruits from
a bowl could look like this: `bowl add apple` or `bowl remove apple`.
Here `add` and `remove` are typical examples for literals. They are fixed and cannot change.
It is also possible to add aliases to literals, for example `a` and `r`.
The fruit itself however, should not be a literal. It would be possible of course, however, there
are many fruits and the logic behind the command is always the same for all fruits. For the fruits you
should definitely use Arguments.

#### 2. Arguments
An Argument is, as the name implies, an argument. For our bowl example we would just create 
one argument named `fruits`. But unlike the literals you do not enter `fruits` when typing the command.
`fruits` is just the name of the argument. Instead, you can enter everything the argument allows you to.
Our fruit argument could for example allow us to type `apple`, `banana` or `orange`. The method that gets
executed when entering the command will be the same for all fruits, but you will have access to the 
specified fruit.
Let's say we expand our bowl command and we want to be able to specify the amount of fruits we want to add to
our bowl for example `bowl add apple 5`. Good luck creating 2,147,483,647 literals for every possible integer.
Fortunately we can just create an integer argument and call it `amount` and we are done. You will also have access
to the amount of fruits that should be added when running the command, even with the correct type!
There are several different already implemented arguments:
1. Boolean Argument: Accepts true / false
2. Double Argument: Accepts any double limited by min and max
3. Integer Argument: Accepts any integer limited by min and max
4. Greedy Argument: Accepts an infinite amount of words. Should be the last part of a command tree
5. Quoted Argument: Accepts a string in quotes
6. Word Argument: Accepts a single word
7. Set Argument: Accepts any element from a predefined set
8. Enum Argument: Accepts any element from an enum
9. Online Player Argument (Paper/Velocity only): Accepts any online player name and provides the player object

With these Nodes / Arguments it is already possible to build quite complex commands. However, you can implement your own types
at any time if needed.

There are also some more features the command structure of the API provides.
1. Optional Nodes: Nodes can be marked as optional and can therefore be skipped
2. Node descriptions: Descriptions can be added to Nodes to describe their use. These descriptions may be used in help messages o.a.
3. Node permissions: Permissions can be added to Nodes. These permissions are then required to use that part of the command. More on that later.
4. And more!

### In Code

Now that we understand how commands are structured and built we can finally look into some more code!

#### The Command Source
A command has to be executed by someone. The command api has to be able to send messages to this someone
and check for permissions. This is why there is the `Source` interface:
```java
public interface Source<M> {
    void sendMessage(M message);
    boolean hasPermission(String permission);
}
```
Everything that executes commands must implement this interface. The API just uses a generic type `S extends Source<>` as the
source for commands, so you can just pass your source implementation. If you see the generic type `S` somewhere in
the project it refers to the source type!

Maybe you noticed the generic type `M`. The CommandAPI tries to be as versatile as possible. Because
of that it does not force you to use for example strings as messages. You can use what every type you
or your already existing system uses. If you see the generic type `M` somewhere in the project it refers
to the message type!

#### The Command interface
The Command interface encapsulates a single command. It holds the tree structure of it and contains
basic functionality to parse and execute commands.

A basic implementation of the Command interface could look like this:
```java
@Getter
public class TestCommand implements Command<TestCommand.TestSource, String, CommandDesign<String>, DefaultLiteralBuilder<TestCommand.TestSource, String>, DefaultArgumentBuilder<?, TestCommand.TestSource, String>> {
    private Literal<TestSource, String> rootNode;
    @Accessors(fluent = true)
    private final DefaultFactory<TestSource, String> factory;
    private final CommandDesign<TestSource> design = new CommandDesign<>(new CommandMessages<>());

    public TestCommand(DefaultFactory<TestSource, String> factory) {
        this.factory = factory;
    }

    public static class TestSource implements Source<String> {
        @Override
        public void sendMessage(String message) {
            System.out.println(message);
        }

        @Override
        public boolean hasPermission(String permission) {
            return true;
        }
    }
}
```
Now that is a lot to talk about, but don't worry, in the end it is quite simple!
The `rootNode` attribute contains the tree structure of the command.
You can ignore the `factory` and `design` attributes for now.

As already explained you need a Source that executes the command. For this example I just
implemented a basic source `TestSource` that sends incoming messages to the console and 
always has permission.

The command interface also requires some generic values. The first one is the source type, 
in our case `TestSource`. The second one is the message type, in our example `String`.
As you can see for example the Literal rootNode also has the source and message types as
generic attributes.

#### Command Design

#### Building the command tree
