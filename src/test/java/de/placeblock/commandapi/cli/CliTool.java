package de.placeblock.commandapi.cli;

import de.placeblock.commandapi.ParseTestCommand;
import de.placeblock.commandapi.core.Command;
import de.placeblock.commandapi.core.parser.ParsedCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CliTool {
    private static final ParseTestCommand command = new ParseTestCommand();

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(System.in));

        // Reading data using readLine
        try {
            while (true) {
                String name = reader.readLine();
                List<ParsedCommand<String>> parsedCommands = command.parse(name, "test");
                command.execute(Command.getBestResult(parsedCommands), "test");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
