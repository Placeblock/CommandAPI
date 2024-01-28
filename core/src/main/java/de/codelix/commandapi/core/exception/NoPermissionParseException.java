package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoPermissionParseException extends ParseException {
    private final Node<?, ?> node;
}
