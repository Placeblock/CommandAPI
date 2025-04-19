package de.codelix.commandapi.core.exception;

import de.codelix.commandapi.core.tree.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ParseException extends Exception implements CommandException {
    private Node<?, ?> node;
}
