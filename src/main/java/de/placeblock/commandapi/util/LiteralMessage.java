package de.placeblock.commandapi.util;

public class LiteralMessage implements Message {
    private final String string;

    public LiteralMessage(String string) {
        this.string = string;
    }

    @Override
    public String getString() {
        return this.string;
    }
}
