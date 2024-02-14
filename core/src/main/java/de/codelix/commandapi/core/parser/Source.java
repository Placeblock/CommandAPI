package de.codelix.commandapi.core.parser;

public interface Source<M> {

    void sendMessage(M message);

    boolean hasPermission(String permission);

}
