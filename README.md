# The CommandAPI

The CommandAPI is ja Java Library for handling user input. It is written very generic so it can be used in various
situations and extended where needed. Although written for minecraft plugins, all Minecraft functionality is inside
own modules and the core module which handles the parsing of commands has nothing to do with Minecraft at all!
You could use it for a native command line application too!
The CommandAPI parses whole words instead of each char individually which makes it much more performant than most of
the other Command-parsing libraries out there!

Have fun using it ;)

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
