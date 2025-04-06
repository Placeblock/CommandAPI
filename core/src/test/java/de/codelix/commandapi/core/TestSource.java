package de.codelix.commandapi.core;

import de.codelix.commandapi.core.parser.Source;

public class TestSource implements Source<String> {
    @Override
    public void sendMessage(String message) {
        System.out.println("Message Received: " + message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }
}
