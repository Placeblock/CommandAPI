package de.codelix.commandapi.core.parser;

import de.codelix.commandapi.core.exception.ParseException;
import de.codelix.commandapi.core.tree.Node;
import de.codelix.commandapi.core.tree.Argument;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@SuppressWarnings("unused")
public class ParsedCommand<S> {
    @Getter
    private final List<Node<S>> nodes = new ArrayList<>();
    private final LinkedHashMap<Argument<?, S>, Object> arguments = new LinkedHashMap<>();
    private final LinkedHashMap<Node<S>, String> parsed = new LinkedHashMap<>();
    @Getter
    @Setter
    private ParseException exception;

    public void storeArgument(Argument<?, S> argument, Object value) {
        this.arguments.put(argument, value);
    }

    public void storeParsed(Node<S> node, String value) {
        this.parsed.put(node, value);
    }

    public Object getArgument(String name) {
        for (Map.Entry<Argument<?, S>, Object> parameterEntry : this.arguments.entrySet()) {
            if (parameterEntry.getKey().getName().equals(name)) {
                return this.arguments.get(parameterEntry.getKey());
            }
        }
        return null;
    }

    public Object getArgument(int index) {
        return this.arguments.values().toArray()[index];
    }

    public String getParsed(Node<S> node) {
        return this.parsed.get(node);
    }

    public void addNode(Node<S> node) {
        this.nodes.add(node);
    }
}
