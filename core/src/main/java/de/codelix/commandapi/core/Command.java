package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Node;

import java.util.LinkedList;
import java.util.List;

public interface Command<S> {

    Node getRootNode();

    default ParsedCommand execute(List<String> input, S source) throws SyntaxException {
        ParseContext ctx = this.createParseContext(input);
        ParsedCommand cmd = new ParsedCommand();
        this.getRootNode().parseRecursive(ctx, cmd);
        return cmd;
    }

    default ParseContext createParseContext(List<String> input) {
        LinkedList<String> linkedInput = new LinkedList<>(input);
        return new ParseContext(linkedInput);
    }

}
