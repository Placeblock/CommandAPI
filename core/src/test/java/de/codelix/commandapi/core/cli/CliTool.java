package de.codelix.commandapi.core.cli;

import de.codelix.commandapi.core.ParseTestCommand;
import de.codelix.commandapi.core.Command;
import de.codelix.commandapi.core.parser.ParsedCommandBranch;

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
                List<ParsedCommandBranch<String>> parsedCommandBranches = command.parse(name, "test");
                command.execute(Command.getBestResult(parsedCommandBranches), "test");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
