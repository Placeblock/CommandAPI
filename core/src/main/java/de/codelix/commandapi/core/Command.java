package de.codelix.commandapi.core;

import de.codelix.commandapi.core.exception.SyntaxException;
import de.codelix.commandapi.core.parser.ParseContext;
import de.codelix.commandapi.core.parser.ParsedCommand;
import de.codelix.commandapi.core.tree.Node;

import java.util.LinkedList;
import java.util.List;

public interface Command<S> {

    Node<S> getRootNode();

    default ParsedCommand<S> execute(List<String> input, S source) throws SyntaxException {
        ParseContext<S> ctx = this.createParseContext(input, source);
        ParsedCommand<S> cmd = new ParsedCommand<>();
        this.getRootNode().parseRecursive(ctx, cmd);
        return cmd;
    }

    default ParseContext<S> createParseContext(List<String> input, S source) {
        LinkedList<String> linkedInput = new LinkedList<>(input);
        return new ParseContext<>(linkedInput, source);
    }

}
