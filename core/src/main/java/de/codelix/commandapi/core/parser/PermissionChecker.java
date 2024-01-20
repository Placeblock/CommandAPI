package de.codelix.commandapi.core.parser;

@FunctionalInterface
public interface PermissionChecker<S extends Source<M>, M> {
    boolean hasPermission(S source, String permission);
}
