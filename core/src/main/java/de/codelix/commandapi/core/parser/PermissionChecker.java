package de.codelix.commandapi.core.parser;

@FunctionalInterface
public interface PermissionChecker<S> {
    boolean hasPermission(S source, String permission);
}
