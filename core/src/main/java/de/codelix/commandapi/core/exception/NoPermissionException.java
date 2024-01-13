package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoPermissionException extends SyntaxException{
    private final Node<?> node;
}
