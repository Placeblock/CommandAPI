package de.codelix.commandapi.core.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@AllArgsConstructor
public class ParseContext<S> {

    @Setter
    private Queue<String> input;

    private final S source;
    private final PermissionChecker<S> permissionChecker;

    public ParseContext<S> copy() {
        return new ParseContext<>(new LinkedList<>(this.input), this.source, this.permissionChecker);
    }

    public String getRemaining() {
        return String.join(" ", this.input);
    }

    public boolean hasPermission(String permission) {
        return permission == null || this.permissionChecker.hasPermission(this.source, permission);
    }
}
